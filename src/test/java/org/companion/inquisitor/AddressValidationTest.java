package org.companion.inquisitor;

import data.Address;
import data.DefaultAddress;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class AddressValidationTest {

    private ValidationRule validationRule;

    public AddressValidationTest() throws IOException {
        File metaResource = new File("src/test/resources/meta_validator.xml");
        File configResource = new File("src/test/resources/validator.xml");
        MetaData metaData = new MetaValidatorFactory().compile(metaResource);
        ValidatorFactory validatorFactory = new ValidatorFactory(metaData);
        Map<String, ValidationRule> validators = validatorFactory.compile(configResource);
        Assert.assertNotNull(validators);

        ValidationRule validationRule = validators.get("POSTAL_CODE_LENGTH");
        Assert.assertNotNull(validationRule);
        this.validationRule = validationRule;
    }

    @Test
    public void caseMissingPostalCode() {
        Address address = new DefaultAddress.Builder()
                .setCity("city0")
                .setState("state0")
                .setStreet("street0")
                .setPostalCode("25700")
                .setCountry("country0")
                .build();

        Assert.assertTrue(validationRule.validate(address));
    }

    @Test
    public void case6CharactersPostalCode() {
        Address address = new DefaultAddress.Builder()
                .setCity("city0")
                .setState("state0")
                .setStreet("street0")
                .setPostalCode("AB25700")
                .setCountry("country0")
                .build();

        Assert.assertTrue(validationRule.validate(address));
    }
}
