package ru.ulstu.is.ontology;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.vocab.OWL2Datatype.Category;
import org.swrlapi.core.SWRLAPIOWLOntology;
import org.swrlapi.core.SWRLAPIRule;
import org.swrlapi.core.SWRLRuleRenderer;
import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.factory.SWRLAPIInternalFactory;

import ru.ulstu.is.utils.StringUtils;

public class OntoHelper {
    private final OWLOntologyManager manager;
    private final OWLOntology ontology;
    private final SWRLAPIOWLOntology swrl;
    private final SWRLRuleRenderer swrlRenderer;
    private final String iriPrefix;

    public OntoHelper(InputStream ontologyStream) {
        try {
            manager = OWLManager.createOWLOntologyManager();
            ontology = manager.loadOntologyFromOntologyDocument(ontologyStream);
            iriPrefix = IriBuilder.create(ontology).getPrefix();
            Optional.ofNullable(manager.getOntologyFormat(ontology))
                    .orElseThrow()
                    .asPrefixOWLOntologyFormat().setDefaultPrefix(iriPrefix);
            swrl = SWRLAPIInternalFactory.createSWRLAPIOntology(ontology);
            swrlRenderer = swrl.createSWRLRuleRenderer();
        } catch (OWLOntologyCreationException | SWRLBuiltInException e) {
            throw new InternalError(e);
        }
    }

    private boolean isIRIEquals(IRI iri, String name) {
        if (iri == null) {
            return false;
        }
        if (!StringUtils.hasText(name)) {
            return false;
        }
        final String iriString = iri.getRemainder().or("");
        return iriString.equalsIgnoreCase(name);
    }

    private String getOntologyAnnotationValue(OntoAnnotation annotation) {
        final Optional<OWLAnnotation> owlAnnotation = ontology.getAnnotations().stream()
                .filter(a -> isIRIEquals(a.getProperty().asOWLAnnotationProperty().getIRI(), annotation.getName()))
                .findAny();
        if (!owlAnnotation.isPresent()) {
            return null;
        }
        return owlAnnotation.get().getValue().asLiteral().orNull().getLiteral();
    }

    private String getAnnotationValue(IRI entity, OntoAnnotation annotation) {
        final var axioms = ontology.getAnnotationAssertionAxioms(entity).stream()
                .filter(a -> isIRIEquals(a.getProperty().asOWLAnnotationProperty().getIRI(), annotation.getName()))
                .toList();
        if (axioms.size() > 1) {
            throw new InternalError(String.format("Multiple annotation %s at %s entity",
                    annotation.getName(), entity.toString()));
        }
        if (axioms.isEmpty()) {
            return null;
        }
        return Optional.ofNullable(axioms.get(0).getValue().asLiteral().orNull().getLiteral())
                .orElseThrow(() -> new InternalError("Empty value"));
    }

    private Set<OWLAnnotationSubject> getAnnotationSubjects(OntoAnnotation annotation) {
        return ontology.getAxioms(AxiomType.ANNOTATION_ASSERTION, Imports.EXCLUDED).stream()
                .filter(axiom -> isIRIEquals(axiom.getProperty().getIRI(), annotation.getName()))
                .map(OWLAnnotationAssertionAxiom::getSubject)
                .collect(Collectors.toSet());
    }

    private boolean isNumberDatatype(OWLDataProperty dataProperty) {
        final var ranges = ontology.getDataPropertyRangeAxioms(dataProperty).stream()
                .toList();
        if (ranges.isEmpty()) {
            return false;
        }
        if (ranges.size() > 1) {
            return false;
        }
        final OWLDatatype datatype = ranges.get(0).getRange().asOWLDatatype();
        return datatype.isBuiltIn() && datatype.getBuiltInDatatype().getCategory() == Category.CAT_NUMBER;
    }

    private Double getDataPropertyValue(OWLDataProperty dataProperty) {
        final var axioms = ontology.getAxioms(AxiomType.DATA_PROPERTY_ASSERTION, Imports.EXCLUDED).stream()
                .filter(axiom -> axiom.getProperty().asOWLDataProperty().getIRI().equals(dataProperty.getIRI()))
                .toList();
        if (axioms.isEmpty()) {
            return null;
        }
        if (axioms.size() > 1) {
            return null;
        }
        return axioms.get(0).getObject().parseDouble();
    }

    private DataProperty toDataProperty(OWLDataProperty property) {
        final IRI iri = property.getIRI();
        final Double value = getDataPropertyValue(property);
        final Double min = StringUtils.toDouble(
                getAnnotationValue(iri, OntoAnnotation.FUZZY_VARIABLE_MINIMUM));
        final Double max = StringUtils.toDouble(
                getAnnotationValue(iri, OntoAnnotation.FUZZY_VARIABLE_MAXIMUM));
        final String aggregation = getAnnotationValue(iri,
                OntoAnnotation.FUZZY_OUTPUT_VARIABLE_AGGREGATION);
        final String defuzzifier = getAnnotationValue(iri,
                OntoAnnotation.FUZZY_OUTPUT_VARIABLE_DEFUZZIFIER);
        return new DataProperty(iri.toString(), value, min, max, aggregation, defuzzifier);
    }

    private AnnotationProperty toAnnotationProperty(OWLNamedIndividual individual) {
        final IRI iri = individual.getIRI();
        return new AnnotationProperty(iri.toString(), getAnnotationValue(iri, OntoAnnotation.FUZZY_TERM));
    }

    private List<String> getNamedIndividuals(List<SWRLAtom> atoms) {
        return atoms.stream()
                .filter(atom -> atom.getIndividualsInSignature().size() == 1)
                .flatMap(atom -> atom.getIndividualsInSignature().stream())
                .map(individual -> individual.getIRI().toString())
                .distinct()
                .toList();
    }

    private SwrlRule toSwrlRule(SWRLAPIRule rule) {
        final var antecedent = getNamedIndividuals(rule.getBodyAtoms());
        final var consequent = getNamedIndividuals(rule.getHeadAtoms());
        return new SwrlRule(swrlRenderer.renderSWRLRule(rule), rule.getRuleName(), antecedent, consequent);
    }

    private Set<DataProperty> getVariables(OntoAnnotation annotation) {
        final var variables = getAnnotationSubjects(annotation);
        return ontology
                .getAxioms(AxiomType.DECLARATION, Imports.EXCLUDED).stream()
                .filter(axiom -> axiom.getEntity().isOWLDataProperty())
                .filter(axiom -> variables.contains(axiom.getEntity().getIRI()))
                .map(axiom -> axiom.getEntity().asOWLDataProperty())
                .filter(this::isNumberDatatype)
                .map(this::toDataProperty)
                .collect(Collectors.toSet());
    }

    public String getPrefix() {
        return iriPrefix;
    }

    public InferenceSettings getInferenceSettings() {
        return new InferenceSettings(
                getOntologyAnnotationValue(OntoAnnotation.FUZZY_INFERENCE),
                getOntologyAnnotationValue(OntoAnnotation.FUZZY_INFERENCE_ACTIVATION),
                getOntologyAnnotationValue(OntoAnnotation.FUZZY_INFERENCE_CONJUNCTION),
                getOntologyAnnotationValue(OntoAnnotation.FUZZY_INFERENCE_DISJUNCTION),
                getOntologyAnnotationValue(OntoAnnotation.FUZZY_INFERENCE_IMPLICATION));
    }

    public Set<AnnotationProperty> getTerms() {
        final var terms = getAnnotationSubjects(OntoAnnotation.FUZZY_TERM);
        return ontology.getAxioms(AxiomType.DECLARATION, Imports.EXCLUDED).stream()
                .filter(axiom -> axiom.getEntity().isOWLNamedIndividual())
                .map(axiom -> axiom.getEntity().asOWLNamedIndividual())
                .filter(individual -> terms.contains(individual.getIRI()))
                .map(this::toAnnotationProperty)
                .collect(Collectors.toSet());
    }

    public Set<DataProperty> getInputVariables() {
        return getVariables(OntoAnnotation.FUZZY_INPUT_VARIABLE);
    }

    public Set<DataProperty> getOutputVariables() {
        return getVariables(OntoAnnotation.FUZZY_OUTPUT_VARIABLE);
    }

    public List<SwrlRule> getSwrlRules() {
        return swrl.getSWRLRules().stream()
                .filter(rule -> !rule.isSQWRLQuery())
                .filter(SWRLAPIRule::isActive)
                .sorted((s1, s2) -> s1.getRuleName().compareToIgnoreCase(s2.getRuleName()))
                .map(this::toSwrlRule)
                .toList();
    }
}
