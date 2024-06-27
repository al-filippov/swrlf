package ru.ulstu.is.swrlf.variable;

import ru.ulstu.is.swrlf.term.Term;
import ru.ulstu.is.utils.FuzzyUtils;

public class VariableMembership {
    private final Term term;
    private final Double membership;

    public VariableMembership(Term term, Double membership) {
        this.term = term;
        this.membership = membership;
    }

    public Term getTerm() {
        return term;
    }

    public Double getMembership() {
        return membership;
    }

    @Override
    public String toString() {
        return "term = " + FuzzyUtils.shortView(term.getName()) + ", membership = " + membership;
    }
}
