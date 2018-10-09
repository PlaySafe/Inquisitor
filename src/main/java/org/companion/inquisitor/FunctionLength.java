package org.companion.inquisitor;

import java.util.List;
import java.util.Map;

class FunctionLength implements Function {

    private final Function preFunction;

    public FunctionLength(FunctionDefinition definition) {
        List<Function> preFunctions = definition.getPreFunctions();
        if (preFunctions.size() == 1) {
            this.preFunction = preFunctions.get(0);
        }
        else {
            throw new IllegalArgumentException("Ambiguous pre-function for 'length': Allow only 1 pre-function");
        }
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) {
        String value = preFunction.perform(input, definitions);
        int length = value.length();
        return String.valueOf(length);
    }
}
