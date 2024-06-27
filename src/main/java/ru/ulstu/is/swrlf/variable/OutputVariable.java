package ru.ulstu.is.swrlf.variable;

import java.util.Iterator;
import java.util.List;

import com.fuzzylite.Op;
import com.fuzzylite.term.Aggregated;

import ru.ulstu.is.swrlf.parameter.Aggregation;
import ru.ulstu.is.swrlf.parameter.Defuzzifier;
import ru.ulstu.is.swrlf.term.Term;
import ru.ulstu.is.utils.FuzzyUtils;

public class OutputVariable extends Variable<com.fuzzylite.variable.OutputVariable> {
    private final Defuzzifier defuzzifier;
    private final Aggregation aggregation;

    public OutputVariable(String name, Double value, Double min, Double max, List<Term> terms,
            Defuzzifier defuzzifier, Aggregation aggregation) {
        super(name, value, min, max, terms);
        this.defuzzifier = defuzzifier;
        this.aggregation = aggregation;
        if (defuzzifier == null) {
            throw new InternalError("Defuzzifier can not be null");
        }
        fuzzyVariable.setDefuzzifier(defuzzifier.get());
        if (aggregation != null) {
            fuzzyVariable.setAggregation(aggregation.get());
        }
    }

    @Override
    protected com.fuzzylite.variable.OutputVariable init() {
        final com.fuzzylite.variable.OutputVariable variable = new com.fuzzylite.variable.OutputVariable(
                FuzzyUtils.escape(name));
        if (min != null) {
            variable.setMinimum(min);
        }
        if (max != null) {
            variable.setMaximum(max);
        }
        terms.forEach(term -> variable.addTerm(term.getFuzzyTerm()));
        return variable;
    }

    @Override
    public Double getValue() {
        return fuzzyVariable.getValue();
    }

    public String getFuzzyOutput() {
        final StringBuilder sb = new StringBuilder();
        final Iterator<Term> it = terms.iterator();
        final Aggregated fuzzyOutput = fuzzyVariable.fuzzyOutput();
        if (it.hasNext()) {
            Term term = it.next();
            double degree = fuzzyOutput.activationDegree(term.getFuzzyTerm());
            sb.append(Op.str(degree)).append("/").append(FuzzyUtils.shortView(term.getName()));
            while (it.hasNext()) {
                term = it.next();
                degree = fuzzyOutput.activationDegree(term.getFuzzyTerm());
                if (Double.isNaN(degree) || Op.isGE(degree, 0.0)) {
                    sb.append(" + ").append(Op.str(degree));
                } else {
                    sb.append(" - ").append(Op.str(Math.abs(degree)));
                }
                sb.append("/").append(FuzzyUtils.shortView(term.getName()));
            }
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "OutputVariable[" + super.toString() + "\n"
                + "defuzzifier=" + defuzzifier + "\n"
                + "aggregation=" + aggregation + "]";
    }
}
