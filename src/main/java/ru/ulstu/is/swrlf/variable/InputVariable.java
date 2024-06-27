package ru.ulstu.is.swrlf.variable;

import java.util.List;

import ru.ulstu.is.swrlf.term.Term;
import ru.ulstu.is.utils.FuzzyUtils;

public class InputVariable extends Variable<com.fuzzylite.variable.InputVariable> {
    public InputVariable(String name, Double value, Double min, Double max, List<Term> terms) {
        super(name, value, min, max, terms);
        if (value == null) {
            throw new InternalError("Input variable value can not be null");
        }
    }

    @Override
    protected com.fuzzylite.variable.InputVariable init() {
        final com.fuzzylite.variable.InputVariable variable = new com.fuzzylite.variable.InputVariable(
                FuzzyUtils.escape(name));
        variable.setValue(value);
        if (min != null) {
            variable.setMinimum(min);
        }
        if (max != null) {
            variable.setMaximum(max);
        }
        terms.forEach(term -> variable.addTerm(term.getFuzzyTerm()));
        return variable;
    }

    public List<VariableMembership> getMemberships() {
        return terms.stream()
                .map(term -> new VariableMembership(term, term.getFuzzyTerm().membership(value)))
                .toList();
    }

    @Override
    public String toString() {
        return "InputVariable[" + super.toString() + "]";
    }
}
