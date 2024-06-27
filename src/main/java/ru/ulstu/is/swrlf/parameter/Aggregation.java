package ru.ulstu.is.swrlf.parameter;

import com.fuzzylite.norm.SNorm;
import com.fuzzylite.norm.s.Maximum;

public enum Aggregation {
    // TODO: Add more aggregation options
    MAXIMUX(new Maximum()),
    NOT_USED(null);

    private final SNorm value;

    private Aggregation(SNorm value) {
        this.value = value;
    }

    public SNorm get() {
        return value;
    }
}
