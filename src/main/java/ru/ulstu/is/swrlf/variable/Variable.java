package ru.ulstu.is.swrlf.variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.ulstu.is.swrlf.term.Term;
import ru.ulstu.is.utils.FuzzyUtils;
import ru.ulstu.is.utils.StringUtils;

abstract class Variable<T extends com.fuzzylite.variable.Variable> {
    protected final String name;
    protected final Double value;
    protected final Double min;
    protected final Double max;
    protected final List<Term> terms = new ArrayList<>();
    protected final T fuzzyVariable;

    protected Variable(String name, Double value, Double min, Double max, List<Term> terms) {
        this.name = name;
        this.value = value;
        this.min = min;
        this.max = max;
        if (terms == null || terms.isEmpty()) {
            throw new InternalError(String.format("Terms is not defined for variable %s", name));
        }
        this.terms.addAll(terms);
        fuzzyVariable = init();
    }

    protected abstract T init();

    public T getFuzzyVariable() {
        return fuzzyVariable;
    }

    public String getName() {
        return name;
    }

    public Double getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Variable<?> other = (Variable<?>) obj;
        return Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return "name=" + FuzzyUtils.shortView(name) + "\n"
                + "value=" + value + "\n"
                + "min=" + min + "\n"
                + "max=" + max + "\n"
                + "terms:\n" + StringUtils.collectionToString(terms);
    }

}
