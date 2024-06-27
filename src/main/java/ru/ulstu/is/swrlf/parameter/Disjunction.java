package ru.ulstu.is.swrlf.parameter;

import com.fuzzylite.norm.SNorm;
import com.fuzzylite.norm.s.Maximum;

public enum Disjunction {
    // TODO: Add more disjunction options
    MAXIMUX(new Maximum()),
    NOT_USED(null);

    private final SNorm value;

    private Disjunction(SNorm value) {
        this.value = value;
    }

    public SNorm get() {
        return value;
    }
}
