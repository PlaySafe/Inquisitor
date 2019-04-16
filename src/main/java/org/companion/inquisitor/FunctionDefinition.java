package org.companion.inquisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Define the meta data compile the function for the builder.
 * this class store data from XML configuration.
 */
public class FunctionDefinition {

    private final String parameter1;
    private final String parameter2;
    private final List<Function> preFunctions;
    private final String logic;

    private FunctionDefinition(Builder builder) {
        this.parameter1 = builder.paramter1;
        this.parameter2 = builder.paramter2;
        this.preFunctions = Collections.unmodifiableList(new ArrayList<>(builder.preFunctions));
        this.logic = builder.logic;
    }

    String getParameter1() {
        return parameter1;
    }

    String getParameter2() {
        return parameter2;
    }

    List<Function> getPreFunctions() {
        return preFunctions;
    }

    String getLogic() {
        return logic;
    }

    static final class Builder {

        private String paramter1;
        private String paramter2;
        private List<Function> preFunctions = new ArrayList<>();
        private String logic;

        Builder setParameter1(String param) {
            this.paramter1 = param;
            return this;
        }

        Builder setParameter2(String param) {
            this.paramter2 = param;
            return this;
        }

        Builder addPreFunction(Function preFunction) {
            this.preFunctions.add(preFunction);
            return this;
        }

        Builder setLogic(String logic) {
            this.logic = logic;
            return this;
        }

        FunctionDefinition build() {
            return new FunctionDefinition(this);
        }
    }

}
