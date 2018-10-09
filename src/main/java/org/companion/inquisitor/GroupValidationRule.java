package org.companion.inquisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

class GroupValidationRule implements ValidationRule {

    private final List<ValidationRule> validationRules;

    GroupValidationRule(List<ValidationRule> validationRules) {
        Objects.requireNonNull(validationRules);
        this.validationRules = Collections.unmodifiableList(new ArrayList<>(validationRules));
    }

    @Override
    public boolean validate(Object input) {
        for (ValidationRule validationRule : validationRules) {
            if (validationRule.validate(input)) {
                return true;
            }
        }
        return false;
    }
}
