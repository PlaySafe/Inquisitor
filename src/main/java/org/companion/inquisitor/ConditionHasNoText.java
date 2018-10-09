package org.companion.inquisitor;

import java.util.Map;

class ConditionHasNoText implements Condition {

    private final Function preFunction;

    public ConditionHasNoText(ConditionDefinition definition) {
        Function preFunction = definition.getValue1();
        if (preFunction == null) {
            throw new IllegalArgumentException("Cannot create condition 'has text' without pre-function");
        }
        this.preFunction = preFunction;
    }

    @Override
    public boolean perform(Object input, Map<String, Map<String, Object>> definitions) {
        String result = preFunction.perform(input, definitions);
        return (result == null) || (result.length() == 0);
    }
}
