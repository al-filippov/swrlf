package ru.ulstu.is.swrlf.parameter;

import com.fuzzylite.norm.TNorm;
import com.fuzzylite.norm.t.Minimum;

public enum Conjunction {
    // TODO: Add more conjunction options
    MINIMUM(new Minimum()),
    NOT_USED(null);

    private final TNorm value;

    private Conjunction(TNorm value) {
        this.value = value;
    }

    public TNorm get() {
        return value;
    }
}
