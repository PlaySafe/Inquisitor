package org.companion.inquisitor;

import java.util.Map;

class ConditionIsNotNull implements Condition {

    private final Function preFunction;

    public ConditionIsNotNull(ConditionDefinition definition) {
        Function preFunction = definition.getValue1();
        if (preFunction == null) {
            throw new IllegalArgumentException("Cannot create condition 'is not null' without a pre-function");
        }
        this.preFunction = preFunction;
    }

    @Override
    public boolean perform(Object input, Map<String, Map<String, Object>> definitions) {
        String result = preFunction.perform(input, definitions);
        return result != null;
    }
}
