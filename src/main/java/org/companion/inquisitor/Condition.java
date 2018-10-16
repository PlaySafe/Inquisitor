package org.companion.inquisitor;

import java.util.Map;

/**
 * General interface for all conditions, the implementation can arbitrary decorate condition as tree.
 * The implementation must provide a constructor for {@link ConditionDefinition}.
 * Nevertheless, the implementation can implement overload constructor as well.
 */
public interface Condition {

    /**
     * Checks all conditions both itself and/or the its dependent conditions.
     *
     * @param input       the arbitrary object for checking condition
     * @param definitions the user definition. 1<upper>st</upper> key is the definition name, and the 2<upper>nd</upper> key is the definition key
     * @return <code>true</code> if match all conditions in the tree, otherwise <code>false</code>
     */
    boolean perform(Object input, Map<String, Map<String, Object>> definitions);

}
