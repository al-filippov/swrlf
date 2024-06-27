package ru.ulstu.is.swrlf.term;

import java.util.Arrays;
import java.util.Objects;

import com.fuzzylite.term.SShape;
import com.fuzzylite.term.Trapezoid;
import com.fuzzylite.term.ZShape;

import ru.ulstu.is.utils.FuzzyUtils;
import ru.ulstu.is.utils.StringUtils;

public class Term {
    private final String name;
    private final String variable;
    private final TermType type;
    private final Double[] args;
    private final com.fuzzylite.term.Term fuzzyTerm;

    public Term(String name, String variable, TermType type, Double[] args) {
        this.name = name;
        this.variable = variable;
        this.type = type;
        this.args = args;
        if (!StringUtils.hasText(name)) {
            throw new InternalError("Term name can not be null or empty");
        }
        if (!StringUtils.hasText(variable)) {
            throw new InternalError("Term variable can not be null or empty");
        }
        fuzzyTerm = init();
    }

    private com.fuzzylite.term.Term init() {
        try {
            final String escapedName = FuzzyUtils.escape(name);
            return switch (type) {
                case SSHAPE -> new SShape(escapedName, args[0], args[1]);
                case TRAPEZOID -> new Trapezoid(escapedName, args[0], args[1], args[2], args[3]);
                case ZSHAPE -> new ZShape(escapedName, args[0], args[1]);
                default -> throw new IllegalArgumentException("Unexpected value: " + type);
            };
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InternalError(
                    String.format("Term %s required %s args not %s",
                            type.name(), type.getRequireArgs(), args.length));
        }
    }

    public String getName() {
        return name;
    }

    public String getVariable() {
        return variable;
    }

    public com.fuzzylite.term.Term getFuzzyTerm() {
        return fuzzyTerm;
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
        Term other = (Term) obj;
        return Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return "Term[name=" + FuzzyUtils.shortView(name) + ", "
                + "variable=" + FuzzyUtils.shortView(variable) + ", "
                + "type=" + type + ", "
                + "args=" + Arrays.toString(args) + "]";
    }

}
