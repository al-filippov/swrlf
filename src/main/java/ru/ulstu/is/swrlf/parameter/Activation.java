package ru.ulstu.is.swrlf.parameter;

import com.fuzzylite.activation.General;
import com.fuzzylite.activation.Highest;
import com.fuzzylite.activation.Lowest;

public enum Activation {
    // TODO: Add more activation options
    GENERAL(new General()),
    LOWEST(new Lowest()),
    HIGHEST(new Highest());

    private com.fuzzylite.activation.Activation value;

    private Activation(com.fuzzylite.activation.Activation value) {
        this.value = value;
    }

    public com.fuzzylite.activation.Activation get() {
        return value;
    }
}
