package org.companion.inquisitor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class ValidateFromMapTest {

    private final ValidationRule validationRule;

    public ValidateFromMapTest() throws IOException {
        File metaResource = new File("src/test/resources/meta_validator.xml");
        File configResource = new File("src/test/resources/validator_from_map.xml");
        MetaData metaData = new MetaValidatorFactory().compile(metaResource);
        ValidatorFactory validatorFactory = new ValidatorFactory(metaData);
        Map<String, ValidationRule> validators = validatorFactory.compile(configResource);
        Assert.assertNotNull(validators);

        validationRule = validators.get("POSTAL_CODE_LENGTH");
    }

    @Test
    public void canValidateValueFromMap() {
        Map<String, Object> data = new HashMap<String, Object>() {{
            put("postalCode", "12345");
        }};

        Assert.assertTrue(validationRule.validate(data));
    }

    @Test
    public void failIfMapHasNoDataButExpectData() {
        Assert.assertFalse(validationRule.validate(new HashMap<String, Object>()));
    }
}
