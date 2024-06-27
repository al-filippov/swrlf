package ru.ulstu.is.swrlf.parameter;

import com.fuzzylite.norm.TNorm;
import com.fuzzylite.norm.t.Minimum;

public enum Implication {
    // TODO: Add more implication options
    MINIMUM(new Minimum()),
    NOT_USED(null);

    private final TNorm value;

    private Implication(TNorm value) {
        this.value = value;
    }

    public TNorm get() {
        return value;
    }
}
