package org.companion.inquisitor;

import java.util.Map;

public interface MetaData {

    String getDefinitionTagItem();


    String getAttributeDefinitionName();


    String getAttributeDefinitionItemKey();


    String getAttributeDefinitionItemValue();


    String getAttributeFunctionName();


    String getAttributeFunctionParameter();


    String getAttributeConditionName();


    String getAttributeConditionValue1();


    String getAttributeConditionValue2();


    /**
     * There are 2 important parts,
     * <ol>
     * <li>Function name : a human readable string refer to the function class</li>
     * <li>Function class : a full package class name refer to the implementation</li>
     * </ol>
     *
     * @return a map that specify between the function name and function class (implementation).
     */
    Map<String, Class<? extends Function>> getMetaFunctions();

    /**
     * There are 2 important parts,
     * <ol>
     * <li>Condition name : a human readable string refer to the condition class</li>
     * <li>Condition class : a full package class name refer to the implementation</li>
     * </ol>
     *
     * @return a map that specify between the condition name and condition class (implementation).
     */
    Map<String, Class<? extends Condition>> getMetaConditions();
}
