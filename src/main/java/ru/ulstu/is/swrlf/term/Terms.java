package ru.ulstu.is.swrlf.term;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Terms {
    private final Map<String, List<Term>> termsMap = new LinkedHashMap<>();
    private final Map<String, Term> termsIndex = new HashMap<>();

    public void addTerm(Term term) {
        termsMap.putIfAbsent(term.getVariable(), new ArrayList<>());
        termsMap.get(term.getVariable()).add(term);
        termsIndex.put(term.getName(), term);
    }

    public List<Term> getTerms(String variable) {
        return termsMap.get(variable);
    }

    public Term getTermByName(String name) {
        return termsIndex.get(name);
    }
}
