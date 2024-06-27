package ru.ulstu.is.swrlf;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fuzzylite.Engine;
import com.fuzzylite.Op;
import com.fuzzylite.defuzzifier.IntegralDefuzzifier;
import com.fuzzylite.defuzzifier.WeightedAverage;

import ru.ulstu.is.ontology.AnnotationProperty;
import ru.ulstu.is.ontology.DataProperty;
import ru.ulstu.is.ontology.InferenceSettings;
import ru.ulstu.is.ontology.OntoHelper;
import ru.ulstu.is.ontology.SwrlRule;
import ru.ulstu.is.swrlf.algorithm.Algorithm;
import ru.ulstu.is.swrlf.algorithm.Settings;
import ru.ulstu.is.swrlf.algorithm.SettingsBuilder;
import ru.ulstu.is.swrlf.parameter.Activation;
import ru.ulstu.is.swrlf.parameter.Aggregation;
import ru.ulstu.is.swrlf.parameter.Conjunction;
import ru.ulstu.is.swrlf.parameter.Defuzzifier;
import ru.ulstu.is.swrlf.parameter.Disjunction;
import ru.ulstu.is.swrlf.parameter.Implication;
import ru.ulstu.is.swrlf.rule.Rule;
import ru.ulstu.is.swrlf.rule.Rules;
import ru.ulstu.is.swrlf.term.Term;
import ru.ulstu.is.swrlf.term.TermType;
import ru.ulstu.is.swrlf.term.Terms;
import ru.ulstu.is.swrlf.variable.InputVariable;
import ru.ulstu.is.swrlf.variable.OutputVariable;
import ru.ulstu.is.utils.EnumUtils;
import ru.ulstu.is.utils.FuzzyUtils;
import ru.ulstu.is.utils.StringUtils;

public class SwrlfEngine implements Closeable {
    private static final Logger log = LoggerFactory.getLogger(SwrlfEngine.class);

    private final InputStream ontologyStream;
    private final OntoHelper ontoHelper;
    private final Engine engine;

    public SwrlfEngine(Path ontologyPath) {
        final File ontologyFile = ontologyPath.toFile();
        try {
            ontologyStream = new FileInputStream(ontologyFile);
            ontoHelper = new OntoHelper(ontologyStream);
            engine = new Engine();
        } catch (FileNotFoundException e) {
            throw new InternalError(String.format("Can't read ontology %s", ontologyFile.getAbsolutePath()));
        }
    }

    private Settings getAlgorithmSettings() {
        final InferenceSettings inferenceSettings = ontoHelper.getInferenceSettings();
        final Algorithm algorithm = EnumUtils.findValue(Algorithm.class, inferenceSettings.getAlgorithm());
        final Activation activation = EnumUtils.findValue(Activation.class, inferenceSettings.getActivation());
        final Conjunction conjunction = EnumUtils.findValue(Conjunction.class, inferenceSettings.getConjunction());
        final Disjunction disjunction = EnumUtils.findValue(Disjunction.class, inferenceSettings.getDisjunction());
        final Implication implication = EnumUtils.findValue(Implication.class, inferenceSettings.getImplication());
        return new SettingsBuilder(algorithm)
                .setActivation(activation)
                .setConjunction(conjunction)
                .setDisjunction(disjunction)
                .setImplication(implication)
                .build();
    }

    private Term toTerm(AnnotationProperty annotation) {
        if (!StringUtils.hasText(annotation.getValue())) {
            throw new InternalError("Annotation value is null or empty");
        }
        final String[] params = annotation.getValue().split(",");
        if (params.length < 3) {
            throw new InternalError(String.format("Invalid term annotation parameters %s", annotation.getValue()));
        }
        final String variable = ontoHelper.getPrefix() + params[0];
        final TermType type = EnumUtils.findValue(TermType.class, params[1]);
        final Double[] args = Arrays.stream(Arrays.copyOfRange(params, 2, params.length))
                .map(Double::parseDouble)
                .toArray(Double[]::new);
        return new Term(annotation.getName(), variable, type, args);
    }

    private InputVariable toInputVariable(DataProperty property, List<Term> terms) {
        return new InputVariable(property.getName(), property.getValue(), property.getMin(), property.getMax(), terms);
    }

    private OutputVariable toOutputVariable(DataProperty property, Settings settings, List<Term> terms) {
        Defuzzifier defuzzifier = EnumUtils.findValue(Defuzzifier.class, property.getDefuzzifier());
        Aggregation aggregation = EnumUtils.findValue(Aggregation.class, property.getAggregation());
        if (defuzzifier == null) {
            defuzzifier = settings.getDefuzzifier();
        }
        if (aggregation == null) {
            aggregation = settings.getAggregation();
        }
        return new OutputVariable(property.getName(), property.getValue(), property.getMin(), property.getMax(), terms,
                defuzzifier, aggregation);
    }

    private List<Term> atomsToTerms(List<String> atoms, Terms terms) {
        final var termsList = atoms.stream()
                .map(terms::getTermByName)
                .filter(Objects::nonNull)
                .toList();
        if (termsList.size() != atoms.size()) {
            return null;
        }
        return termsList;
    }

    private Rule toRule(SwrlRule swrlRule, Terms terms) {
        var antecedent = atomsToTerms(swrlRule.getAntecedent(), terms);
        var consequent = atomsToTerms(swrlRule.getConsequent(), terms);
        if (antecedent == null || consequent == null) {
            return null;
        }
        return new Rule(swrlRule.getRule(), antecedent, consequent);
    }

    private Terms getTerms() {
        final Set<AnnotationProperty> annotations = ontoHelper.getTerms();
        if (annotations.isEmpty()) {
            throw new InternalError("Terms is not defined");
        }
        final Terms terms = new Terms();
        annotations.forEach(annotation -> terms.addTerm(toTerm(annotation)));
        return terms;
    }

    private Set<InputVariable> getInputVariables(Terms terms) {
        final Set<DataProperty> dataProperties = ontoHelper.getInputVariables();
        if (dataProperties.isEmpty()) {
            throw new InternalError("Input variables is not defined");
        }
        return dataProperties.stream()
                .map(property -> toInputVariable(property, terms.getTerms(property.getName())))
                .collect(Collectors.toSet());
    }

    private Set<OutputVariable> getOutputVariables(Terms terms, Settings settings) {
        final Set<DataProperty> dataProperties = ontoHelper.getOutputVariables();
        if (dataProperties.isEmpty()) {
            throw new InternalError("Output variables is not defined");
        }
        return dataProperties.stream()
                .map(property -> toOutputVariable(property, settings, terms.getTerms(property.getName())))
                .collect(Collectors.toSet());
    }

    private List<Rule> getRules(Terms terms) {
        final List<SwrlRule> swrlRules = ontoHelper.getSwrlRules();
        return swrlRules.stream()
                .map(rule -> toRule(rule, terms))
                .filter(Objects::nonNull)
                .toList();
    }

    public void run() {
        final Settings settings = getAlgorithmSettings();
        log.info("Current algorithm settings are:\n{}", settings);
        log.info("");

        final Terms terms = getTerms();
        final Set<InputVariable> inputVariables = getInputVariables(terms);
        final Set<OutputVariable> outputVariables = getOutputVariables(terms, settings);

        inputVariables.forEach(variable -> engine.addInputVariable(variable.getFuzzyVariable()));
        log.info("Input variables:\n{}", StringUtils.collectionToString(inputVariables));
        log.info("");

        outputVariables.forEach(variable -> engine.addOutputVariable(variable.getFuzzyVariable()));
        log.info("Output variables:\n{}", StringUtils.collectionToString(outputVariables));
        log.info("");

        engine.getOutputVariables().forEach(variable -> {
            if (!(variable.getDefuzzifier() instanceof IntegralDefuzzifier)) {
                return;
            }
            if (!Op.isFinite(variable.getMinimum() + variable.getMaximum())) {
                variable.setDefuzzifier(new WeightedAverage());
            }
        });

        log.info("Fuzzyfication:");
        inputVariables.forEach(variable -> variable.getMemberships()
                .forEach(membership -> log.info("{}: {}", variable.getValue(), membership)));
        log.info("");

        final Rules rules = new Rules(getRules(terms), engine, settings);
        engine.addRuleBlock(rules.getFuzzyRules());
        log.info("Rules:\n{}", rules);

        engine.process();

        log.info("Aggregation and activation:");
        rules.getActivations().forEach(activation -> log.info("{}", activation));
        log.info("");

        outputVariables.forEach(variable -> log.info(
                "{} = {}", FuzzyUtils.shortView(variable.getName()), variable.getFuzzyOutput()));
        log.info("");

        log.info("Accumulation and Defuzzyfication: ");
        outputVariables.forEach(variable -> log.info(
                "{} = {}", FuzzyUtils.shortView(variable.getName()), variable.getValue()));
    }

    @Override
    public void close() throws IOException {
        ontologyStream.close();
    }
}
