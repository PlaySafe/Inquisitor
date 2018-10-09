package org.companion.inquisitor;

import java.util.List;
import java.util.Map;

/**
 * <p>The negative index (-X): return since first character until the last X character exclude the last character e.g 9876543 substring -3 = 9876</p><br/>
 * <p>The positive index (+X): return since character X to the last character e.g. 123456 substring 2 = 3456</p>
 */
class FunctionSubstring implements Function {

    private final Function preFunction;
    private final int position;

    public FunctionSubstring(FunctionDefinition definition) {
        List<Function> preFunctions = definition.getPreFunctions();
        if (preFunctions.size() == 1) {
            this.preFunction = preFunctions.get(0);
        }
        else {
            throw new IllegalArgumentException("Ambiguous pre-function for 'substring': Allow only 1 pre-function");
        }
        this.position = Integer.valueOf(definition.getParam());
    }

    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) {
        String value = preFunction.perform(input, definitions);
        int length = value.length();
        return (position > 0) ? value.substring(position, length) : value.substring(0, length + position);
    }
}
