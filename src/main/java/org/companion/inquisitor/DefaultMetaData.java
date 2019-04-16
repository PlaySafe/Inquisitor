package org.companion.inquisitor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class DefaultMetaData implements MetaData {

    private final String tagDefinitionData;
    private final String attributeDefinitionName;
    private final String attributeDefinitionItemKey;
    private final String attributeDefinitionItemValue;

    private final String attributeFunctionName;
    private final String attributeFunctionParameter1;
    private final String attributeFunctionParameter2;

    private final String attributeConditionName;
    private final String attributeConditionValue1;
    private final String attributeConditionValue2;

    private final Map<String, Class<? extends Function>> metaFunctions;
    private final Map<String, Class<? extends Condition>> metaConditions;

    private DefaultMetaData(Builder builder) {
        this.tagDefinitionData = builder.tagDefinitionData;
        this.attributeDefinitionName = builder.attributeDefinitionName;
        this.attributeDefinitionItemKey = builder.attributeDefinitionItemKey;
        this.attributeDefinitionItemValue = builder.attributeDefinitionItemValue;

        this.attributeFunctionName = builder.attributeFunctionName;
        this.attributeFunctionParameter1 = builder.attributeFunctionParameter1;
        this.attributeFunctionParameter2 = builder.attributeFunctionParameter2;

        this.attributeConditionName = builder.attributeConditionName;
        this.attributeConditionValue1 = builder.attributeConditionValue1;
        this.attributeConditionValue2 = builder.attributeConditionValue2;

        this.metaFunctions = Collections.unmodifiableMap(new HashMap<>(builder.metaFunctions));
        this.metaConditions = Collections.unmodifiableMap(new HashMap<>(builder.metaConditions));
    }

    @Override
    public String getDefinitionTagItem() {
        return tagDefinitionData;
    }

    @Override
    public String getAttributeDefinitionName() {
        return attributeDefinitionName;
    }

    @Override
    public String getAttributeDefinitionItemKey() {
        return attributeDefinitionItemKey;
    }

    @Override
    public String getAttributeDefinitionItemValue() {
        return attributeDefinitionItemValue;
    }

    @Override
    public String getAttributeFunctionName() {
        return attributeFunctionName;
    }

    @Override
    public String getAttributeFunctionParameter1() {
        return attributeFunctionParameter1;
    }

    @Override
    public String getAttributeFunctionParameter2() {
        return attributeFunctionParameter2;
    }

    @Override
    public String getAttributeConditionName() {
        return attributeConditionName;
    }

    @Override
    public String getAttributeConditionValue1() {
        return attributeConditionValue1;
    }

    @Override
    public String getAttributeConditionValue2() {
        return attributeConditionValue2;
    }

    @Override
    public Map<String, Class<? extends Function>> getMetaFunctions() {
        return metaFunctions;
    }

    @Override
    public Map<String, Class<? extends Condition>> getMetaConditions() {
        return metaConditions;
    }

    static class Builder {

        private String tagDefinitionData;
        private String attributeDefinitionName;
        private String attributeDefinitionItemKey;
        private String attributeDefinitionItemValue;

        private String attributeFunctionName;
        private String attributeFunctionParameter1;
        private String attributeFunctionParameter2;

        private String attributeConditionName;
        private String attributeConditionValue1;
        private String attributeConditionValue2;

        private Map<String, Class<? extends Function>> metaFunctions;
        private Map<String, Class<? extends Condition>> metaConditions;

        Builder() {
            this.metaFunctions = new HashMap<>();
            this.metaConditions = new HashMap<>();
        }

        Builder setDefinitionItemTag(String tagDefinitionData) {
            this.tagDefinitionData = tagDefinitionData;
            return this;
        }

        Builder setAttributeDefinitionName(String definitionName) {
            this.attributeDefinitionName = definitionName;
            return this;
        }

        Builder setAttributeDefinitionItemKey(String attributeItemKey) {
            this.attributeDefinitionItemKey = attributeItemKey;
            return this;
        }

        Builder setAttributeDefinitionItemValue(String attributeItemValue) {
            this.attributeDefinitionItemValue = attributeItemValue;
            return this;
        }

        Builder setAttributeFunctionName(String attributeFunctionName) {
            this.attributeFunctionName = attributeFunctionName;
            return this;
        }

        Builder setAttributeFunctionParameter1(String parameter) {
            this.attributeFunctionParameter1 = parameter;
            return this;
        }

        Builder setAttributeFunctionParameter2(String paramter) {
            this.attributeFunctionParameter2 = paramter;
            return this;
        }

        Builder setAttributeConditionName(String attributeConditionName) {
            this.attributeConditionName = attributeConditionName;
            return this;
        }

        Builder setAttributeConditionValue1(String attributeConditionValue1) {
            this.attributeConditionValue1 = attributeConditionValue1;
            return this;
        }

        Builder setAttributeConditionValue2(String attributeConditionValue2) {
            this.attributeConditionValue2 = attributeConditionValue2;
            return this;
        }

        Builder setMetaFunctions(Map<String, String> metaFunctions) throws ClassNotFoundException {
            for (Map.Entry<String, String> eachMeta : metaFunctions.entrySet()) {
                String className = eachMeta.getValue();
                Class<? extends Function> functionClass = (Class<? extends Function>) Class.forName(className);
                this.metaFunctions.put(eachMeta.getKey(), functionClass);
            }
            return this;
        }

        Builder setMetaConditions(Map<String, String> metaConditions) throws ClassNotFoundException {
            for (Map.Entry<String, String> eachMeta : metaConditions.entrySet()) {
                String className = eachMeta.getValue();
                Class<? extends Condition> functionClass = (Class<? extends Condition>) Class.forName(className);
                this.metaConditions.put(eachMeta.getKey(), functionClass);
            }
            return this;
        }

        MetaData build() {
            return new DefaultMetaData(this);
        }
    }
}
