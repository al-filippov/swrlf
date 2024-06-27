package ru.ulstu.is.swrlf.algorithm;

import java.util.Optional;

import ru.ulstu.is.swrlf.parameter.Activation;
import ru.ulstu.is.swrlf.parameter.Conjunction;
import ru.ulstu.is.swrlf.parameter.Disjunction;
import ru.ulstu.is.swrlf.parameter.Implication;

public class SettingsBuilder {
    private final Settings settings;

    public SettingsBuilder(Algorithm algorithm) {
        this.settings = Optional.ofNullable(algorithm)
                .map(Algorithm::getSettings)
                .orElse(new Settings());
    }

    public SettingsBuilder setActivation(Activation activation) {
        if (activation != null) {
            settings.setActivation(activation);
        }
        return this;
    }

    public SettingsBuilder setConjunction(Conjunction conjunction) {
        if (conjunction != null) {
            settings.setConjunction(conjunction);
        }
        return this;
    }

    public SettingsBuilder setDisjunction(Disjunction disjunction) {
        if (disjunction != null) {
            settings.setDisjunction(disjunction);
        }
        return this;
    }

    public SettingsBuilder setImplication(Implication implication) {
        if (implication != null) {
            settings.setImplication(implication);
        }
        return this;
    }

    public Settings build() {
        if (settings.getActivation() == null && settings.getConjunction() == null
                && settings.getDisjunction() == null && settings.getImplication() == null) {
            throw new InternalError("Inference algorithm settings is not defined");
        }
        return settings;
    }
}
