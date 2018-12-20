# Welcome to Inquisitor Project

> A lightweight framework for complex validation logic.

As we know that there are many rules, and conditions to validate an object for example,
There is an Address Object, but this object uses by many contexts
* The mail context, you need a full describe address (base on country)
* The user address, you might need just country

## Get Started
Add dependency

Maven
```
<dependency>
    <groupId>io.github.playsafe</groupId>
    <artifactId>inquisitor</artifactId>
    <version>1.0.0</version>
</dependency>
```
Gradle
```
compile group: 'io.github.playsafe', name: 'inquisitor', version: '1.0.0'
```
The repository is [https://mvnrepository.com/artifact/io.github.playsafe/inquisitor](https://mvnrepository.com/artifact/io.github.playsafe/inquisitor)

## Prerequisite
1. Java 8 or above
2. Basic knowledge of xml format

## Understand the problem this project can solve
In the enterprise project, you might have to deal with the complex validation rules 
to validate the same object in different context for example, the mail address require full describe 
while the user address might require only city but those 2 context use the same Address object.

## Step 1: Create a meta data file
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Meta>
    <Definition reference-to-name="name" reference-item-tag="Item"
                reference-item-key="key" reference-item-value="value" />
    <FunctionAttribute reference-to-name="name" reference-to-parameter="param" />
    <ConditionAttribute reference-to-name="name"
                        reference-to-parameter1="value1" reference-to-parameter2="value2" />
    <Conditions>
        <Condition name="has_text" class="org.companion.inquisitor.ConditionHasText" />
        <Condition name="equals" class="org.companion.inquisitor.ConditionEquals" />
        <Condition name="greater_than" class="org.companion.inquisitor.ConditionGreaterThan" />
        <Condition name="is_letter" class="org.companion.inquisitor.ConditionIsLetter" />
    </Conditions>
    <Functions>
        <Function name="get" class="org.companion.inquisitor.FunctionGet" />
        <Function name="char_at" class="org.companion.inquisitor.FunctionCharAt" />
        <Function name="length" class="org.companion.inquisitor.FunctionLength" />
    </Functions>
</Meta>
```
This file will define the available functions and conditions

1. The configuration of **\<FunctionAttribute\>** refers to the configuration attributes of **\<Function\>**
   * **reference-to-name="name"** refers to attribute of **\<Function name="..."\>**
   * **reference-to-parameter="param"** refers to attribute of **\<Function param="..."\>**
2. The configuration of **\<ConditionAttribute\>** refers to the configuration attributes of **\<Condition\>**
   * **reference-to-name="name"** refers to attribute of **\<Condition name="..."\>**
   * **reference-to-parameter1="value1"**, and **reference-to-parameter2="value2"** refer to **\<Condition value1="..." value2="..."\>**
3. The configuration of **\<Definition\>** refers to the configuration of **\<Definition\>** and its attributes
   * **reference-to-name="name"** refers to **\<Definition name="..."\>**
   * **reference-item-tag="Item"** refers to **\<Item\>**
   * **reference-item-key="key"** refers to **\<Item key="..."\>**
   * **reference-item-value="value"** refers to **\<Item value="..."\>**
4. The **\<Condition\>** uses to define the available conditions, so does the **\<Function\>**

## Step2: Create validation rules
```
<ValidationRules>
    <!-- Rule 1: Postal Code's length = 5 -->
    <ValidationRule group="POSTAL_CODE_LENGTH">
        <Condition name="has_text">
            <Function name="get" param="@{postalCode}" />
        </Condition>
        <Condition name="equals" value2="5">
            <Function name="length">
                <Function name="get" param="@{postalCode}" />
            </Function>
        </Condition>
    </ValidationRule>

    <!-- Rule 2: Postal Code's length > 5 -->
    <ValidationRule group="POSTAL_CODE_LENGTH">
        <Condition name="has_text">
            <Function name="get" param="@{postalCode}" />
        </Condition>
        <Condition name="greater_than" value2="5">
            <Function name="length">
                <Function name="get" param="@{postalCode}" />
            </Function>
        </Condition>
        <Condition name="is_letter">
            <Function name="char_at" param="0">
                <Function name="get" param="@{postalCode}" />
            </Function>
        </Condition>
        <Condition name="is_letter">
            <Function name="char_at" param="1">
                <Function name="get" param="@{postalCode}" />
            </Function>
        </Condition>
    </ValidationRule>
</ValidationRules>
```

**Notice:** The multiple **\<Condition\>** under **\<ValidationRule\>** consider as **and** condition, 
but the same validation rule group consider **or** condition. 
As the code above there is only 1 group (POSTAL_CODE_LENGTH) to validate if
**Postal code have 5 characters or starts with 2 letters if longer than 5**

Configuration file tips
* Specify properties using format **${}**. 
  * The **${ABC.DEF}** refers to properties key **ABC.DEF**
  * Properties value will get from System properties, so you have to load all properties first
* Specify data using format **@{}**. 
  * The **@{name}** refers to method **getName()** of the object input
* Specify definition using format **#{}**. 
  * The **#{XYZ}** refers to definition name **XYZ** 
  * The **#{XYZ.ABC}** refers to definition key **ABC** of definition name **XYZ** 
* Add a the new line using ${line.separator}

## Step3: Create a new class or interface for this config.
```
public class Address {

    public String getStreet() {
        ...
    }

    public String getState() {
        ...
    }

    public String getCity() {
        ...
    }

    public String getPostalCode() {
        ...
    }
    
    public String getCountry() {
        ...
    }

```

## Step 4: Write Java code to generate
First, you need to load both meta data and configuration first, as the code below

```
File metaResource = new File(<path to meta data file>);
File configResource = new File(<path to config file>);
MetaData metaData = new MetaValidatorFactory().compile(metaResource);
ValidatorFactory validatorFactory = new ValidatorFactory(metaData);
Map<String, ValidationRule> validators = validatorFactory.compile(configResource);
```

Then, you can choose the label generator using group as the key 

```
// Set data to your data object first
Address address = new Address();

ValidationRule validationRule = validators.get("POSTAL_CODE_LENGTH");
boolean isValid = validationRule.validate(address);
```


## Create your custom Function
You can create a new function yourselves by
1. Creating a new class and implement Function interface
2. Define a constructor that require FunctionDefinition
3. Register your custom function in meta data file


```
public class MyCustomFunction implements Function {
            
    public MyCustomFunction(FunctionDefinition definition) {
        // You can retrieve configuration from definition
    }
    
    @Override
    public String perform(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        // Perform the function logic directly, or check the existing of pre-condition first
        // ConditionNotMatchException should be thrown when pre-condition doesn't match
    }
}
```

Notice:
* You can get many pre-conditions from definition, it corresponds to the condition tag in the configuration
* The **Map<String, Map<String, Object>> definitions** is the data of definition tag in the configuration
* The **Object input** is the data object that send from user
* **Ignore the definition.getLogic()**, it is use to select the right implementation from meta data
* The value1, and value2 refer to the parameter1 and parameter2 in the configuration respectively
* You don't need to define class to be public, but you need a public constructor

## Create your custom Condition
You can create a new condition yourselves by 
1. Creating a new class and implement Condition interface
2. Define a constructor that require ConditionDefinition
3. Register your custom condition in meta data file

```
public class MyCustomCondition implements Condition {
        
    public MyCustomCondition(ConditionDefinition definition){
        // You can retrieve configuration from definition
    }
    
    @Override
    public boolean matches(Object input, Map<String, Map<String, Object>> definitions) throws ConditionNotMatchException {
        // return the result
    }
}
```

Notice:
* You can get many pre-conditions from definition, it corresponds to the condition tag in the configuration
* The **Map<String, Map<String, Object>> definitions** is the data of definition tag in the configuration
* The **Object input** is the data object that send from user
* **Ignore the definition.getLogic()**, it is use to select the right implementation from meta data
* The value1, and value2 refer to the parameter1 and parameter2 in the configuration respectively
* You don't need to define class to be public, but you need a public constructor
* org.companion.inquisitor.VariableReflector can help you when
   * You want value from object
   * You want value of properties

## Available Function

    org.companion.inquisitor.FunctionGet

Returns value from the specific definition, properties, specific field, or the value itself corresponds to the configuration.

Since: 1.0.0

Support a map value since 1.0.2

---
    org.companion.inquisitor.FunctionLength

Returns length of the string

Since: 1.0.0

---
    org.companion.inquisitor.FunctionSubstring

 * The negative index (-X): return since first character until the last X character exclude the last character e.g 9876543 substring -3 = 9876
 * The positive index (+X): return since character X to the last character e.g. 123456 substring 2 = 3456

Since: 1.0.0

---
    org.companion.inquisitor.FunctionCutOff

 * The negative index (-X): return last X character e.g 9876543 cut off -3 = 543
 * The positive index (+X): return first X character e.g. 123456 cut off 2 = 12

Since: 1.0.0

---
    org.companion.inquisitor.FunctionCharAt

Returns a character at the specific index

Since: 1.0.0


## Available Condition
    org.companion.inquisitor.ConditionEquals

Returns true if 2 parameters are consider equals, otherwise false

Since: 1.0.0

---
    org.companion.inquisitor.ConditionNotEquals

Returns true if 2 parameters are consider not equals, otherwise false

Since: 1.0.0

---
    org.companion.inquisitor.ConditionLessThan

Returns true if parameter1 < parameter2, otherwise false

Since: 1.0.0

---
    org.companion.inquisitor.ConditionLessThanEquals
    
Returns true if parameter1 <= parameter2, otherwise false

Since: 1.0.0

---
    org.companion.inquisitor.ConditionGreaterThan

Returns true if parameter1 > parameter2, otherwise false

Since: 1.0.0

---
    org.companion.inquisitor.ConditionGreaterThanEquals

Returns true if parameter1 >= parameter2, otherwise false

Since: 1.0.0

---
    org.companion.inquisitor.ConditionIsNull

Returns true if parameter1 is null, otherwise false

Since: 1.0.0

---
    org.companion.inquisitor.ConditionIsNotNull

Returns true if parameter1 is not null, otherwise false

Since: 1.0.0

---
    org.companion.inquisitor.ConditionHasText

Returns true if parameter1 has text (length > 0), otherwise false

Since: 1.0.0

---
    org.companion.inquisitor.ConditionHasNoText

Returns true if parameter1 has no text (null or length = 0), otherwise false

Since: 1.0.0

---
    org.companion.inquisitor.ConditionIsLetter
    
Returns true the whole strings has only letter, otherwise false

Since: 1.0.0

## License
This project is licensed under the Apache 2.0 License - see the [LICENSE](https://github.com/PlaySafe/impresario/blob/master/LICENSE) file for details