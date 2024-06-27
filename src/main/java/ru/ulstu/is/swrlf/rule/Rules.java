package ru.ulstu.is.swrlf.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fuzzylite.Engine;
import com.fuzzylite.rule.RuleBlock;

import ru.ulstu.is.swrlf.algorithm.Settings;
import ru.ulstu.is.utils.StringUtils;

public class Rules {
    private final List<Rule> rulesList = new ArrayList<>();
    private final Map<Rule, com.fuzzylite.rule.Rule> rulesIndex = new HashMap<>();
    private final RuleBlock fuzzyRules;

    public Rules(List<Rule> rules, Engine engine, Settings settings) {
        if (settings == null) {
            throw new InternalError("Algorithm settings can not be null");
        }
        if (rules == null || rules.isEmpty()) {
            throw new InternalError("Rules list can not be null or empty");
        }
        rulesList.addAll(rules);
        fuzzyRules = init(engine, settings);
    }

    private RuleBlock init(Engine engine, Settings settings) {
        final RuleBlock block = new RuleBlock();
        block.setActivation(settings.getActivation().get());
        block.setConjunction(settings.getConjunction().get());
        block.setDisjunction(settings.getDisjunction().get());
        block.setImplication(settings.getImplication().get());
        rulesList.forEach(rule -> {
            final var fuzzyRule = com.fuzzylite.rule.Rule.parse(rule.get(), engine);
            rulesIndex.put(rule, fuzzyRule);
            block.addRule(fuzzyRule);
        });
        return block;
    }

    public RuleBlock getFuzzyRules() {
        return fuzzyRules;
    }

    public List<RuleActivation> getActivations() {
        return rulesList.stream()
                .map(rule -> new RuleActivation(rule, rulesIndex.get(rule).getActivationDegree()))
                // .sorted(Comparator.comparingDouble(RuleActivation::getActivation).reversed())
                .toList();
    }

    @Override
    public String toString() {
        return StringUtils.collectionToString(rulesList);
    }
}
