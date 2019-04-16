package org.companion.inquisitor;

import java.util.List;
import java.util.Map;

class FunctionCharAt implements Function {

    private final Function preFunction;
    private final int position;

    public FunctionCharAt(FunctionDefinition definition) {
        List<Function> preFunctions = definition.getPreFunctions();
        if (preFunctions.size() == 1) {
            this.preFunction = preFunctions.get(0);
        }
        else {
            throw new IllegalArgumentException("Ambiguous pre-function for 'char at': Allow only 1 pre-function");
        }
        this.position = Integer.valueOf(definition.getParameter1());
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) {
        String value = preFunction.perform(input, definitions);
        char c = (position >= 0) ? value.charAt(position) : value.charAt(value.length() + position);
        return String.valueOf(c);
    }
}
