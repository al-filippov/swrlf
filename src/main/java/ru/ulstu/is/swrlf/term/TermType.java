package ru.ulstu.is.swrlf.term;

public enum TermType {
    // TODO: Add more function types
    ZSHAPE(2),
    TRAPEZOID(4),
    SSHAPE(2);

    private int requireArgs;

    private TermType(int requireArgs) {
        this.requireArgs = requireArgs;
    }

    public int getRequireArgs() {
        return requireArgs;
    }
}
