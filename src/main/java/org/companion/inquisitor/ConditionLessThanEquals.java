package org.companion.inquisitor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

class ConditionLessThanEquals implements Condition {

    private final Function function1;
    private final Function function2;

    public ConditionLessThanEquals(ConditionDefinition definition) {
        Objects.requireNonNull(definition);
        this.function1 = definition.getValue1();
        this.function2 = definition.getValue2();
    }

    @Override
    public boolean perform(Object input, Map<String, Map<String, Object>> definitions) {
        String resultFunction1 = function1.perform(input, definitions);
        String resultFunction2 = function2.perform(input, definitions);
        BigDecimal result1 = new BigDecimal(resultFunction1);
        BigDecimal result2 = new BigDecimal(resultFunction2);
        return result1.subtract(result2).signum() <= 0;
    }
}
