# This project is no longer support. It has been merged to impresario project. [https://mvnrepository.com/artifact/io.github.playsafe/impresario](https://mvnrepository.com/artifact/io.github.playsafe/impresario)

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
    <version>1.0.3</version>
</dependency>
```
Gradle
```
compile group: 'io.github.playsafe', name: 'inquisitor', version: '1.0.3'
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
    <FunctionAttribute reference-to-name="name" 
                        reference-to-parameter1="param1" reference-to-parameter2="param2" />
    <ConditionAttribute reference-to-name="name"
                        reference-to-parameter1="param1" reference-to-parameter2="param2" />
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
        <Function name="substring" class="org.companion.inquisitor.FunctionSubstring" />
        <Function name="cut_off" class="org.companion.inquisitor.FunctionCutOff" />
    </Functions>
</Meta>
```
This file will define the available functions and conditions

1. The configuration of **\<FunctionAttribute\>** refers to the configuration attributes of **\<Function\>**
   * **reference-to-name="name"** refers to attribute of **\<Function name="..."\>**
   * **reference-to-parameter1="param1"**, and **reference-to-parameter2="param2" refer to attribute of **\<Function param1="..." param2="..."\>**
2. The configuration of **\<ConditionAttribute\>** refers to the configuration attributes of **\<Condition\>**
   * **reference-to-name="name"** refers to attribute of **\<Condition name="..."\>**
   * **reference-to-parameter1="param1"**, and **reference-to-parameter2="param2"** refer to **\<Condition param1="..." param2="..."\>**
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
        <Definitions>
            <Definition name="EXPECT_LENGTH">
                <Item key="Postal" value="5" />
            </Definition>
        </Definitions>
        <Condition name="has_text">
            <Function name="get" param1="@{postalCode}" />
        </Condition>
        
        <!-- check if @{postalCode}.length == #{EXPECT_LENGTH.Postal} -->
        <Condition name="equals">
            <Function name="length">
                <Function name="get" param1="@{postalCode}" />
            </Function>
            <Function name="get" param1="#{EXPECT_LENGTH.Postal}" />
        </Condition>
    </ValidationRule>

    <!-- Rule 2: Postal Code's length > 5 -->
    <ValidationRule group="POSTAL_CODE_LENGTH">
        <Condition name="has_text">
            <Function name="get" param1="@{postalCode}" />
        </Condition>
        <Condition name="greater_than" param2="5">
            <Function name="length">
                <Function name="get" param1="@{postalCode}" />
            </Function>
        </Condition>
        <Condition name="is_letter">
            <Function name="char_at" param1="0">
                <Function name="get" param1="@{postalCode}" />
            </Function>
        </Condition>
        <Condition name="is_letter">
            <Function name="char_at" param1="1">
                <Function name="get" param1="@{postalCode}" />
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
* The param1, and param2 refer to the parameter1 and parameter2 in the configuration respectively
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
* The param1, and param2 refer to the parameter1 and parameter2 in the configuration respectively
* You don't need to define class to be public, but you need a public constructor
* org.companion.inquisitor.VariableReflector can help you when
   * You want value from object
   * You want value of properties

## Available Function

    org.companion.inquisitor.FunctionGet

Returns value from the specific definition, properties, specific field, or the value itself corresponds to the configuration.

Example Configuration
```
<Function name="get" param1="@{fieldA}"/>
```
```
<Function name="get" param1="#{DefinitionName.Key}>
    <Condition name="has_text" param1="@{fieldA} />
</Function>
```

---------------------------------------------------------------------------------------------------
    org.companion.inquisitor.FunctionLength

Returns length of the string

Example Configuration
```
<Function logic="length">
    <Function logic="get" param1="@{city}" />
</Function>
```
From this config, assume that `@{city} = "ABCDE"`. The result will be `5`

---------------------------------------------------------------------------------------------------
    org.companion.inquisitor.FunctionSubstring

 * The negative index (-X): return since first character until the last X character exclude the last character e.g 9876543 substring -3 = 9876
 * The positive index (+X): return since character X to the last character e.g. 123456 substring 2 = 3456

Example Configuration
```
<Function logic="substring" param1="-3">
    <Function logic="substring" param1="2">
        <Function logic="get" param1="@{param}" />
    </Function>
</Function>
```
From this config, assume that `@{param} = "Hello World"`. <br>
The 1st substring will result `llo World` <br>
The 2nd will take result from the 1st, the result will be `llo Wo`

---------------------------------------------------------------------------------------------------
    org.companion.inquisitor.FunctionCutOff

 * The negative index (-X): return last X character e.g 9876543 cut off -3 = 543
 * The positive index (+X): return first X character e.g. 123456 cut off 2 = 12

Example Configuration
```
<Function logic="cut_off" param1="5">
    <Function logic="get" param1="@{name}" />
</Function>
```
From this config, assume that `@{name} = "Tony Stark"`. The result will be `Stark`

---------------------------------------------------------------------------------------------------
    org.companion.inquisitor.FunctionCharAt

Returns a character at the specific index

Example Configuration
```
<Function logic="char_at" param1="-2">
    <Function logic="get" param1="@{param}" />
</Function>
```
From this config, assume that `@{param} = "ABCDE"`. The result will be `D`

---------------------------------------------------------------------------------------------------

## Available Condition
    org.companion.inquisitor.ConditionEquals

Returns true if 2 parameters are consider equals, otherwise false

Example Configuration
```
<Condition logic="equals">
    <Function logic="get" param1="@{param1}" />
    <Function logic="get" param1="@{param2}" />
</Condition>
```
From this config, The result will be `true` if `@{param1}.equals(@{param2})`
otherwise `false`

---------------------------------------------------------------------------------------------------
    org.companion.inquisitor.ConditionNotEquals

Returns true if 2 parameters are consider not equals, otherwise false

Example Configuration
```
<Condition logic="not_equals">
    <Function logic="get" param1="@{param1}" />
    <Function logic="get" param1="@{param2}" />
</Condition>
```
From this config, The result will be `true` if `!@{param1}.equals(@{param2})`
otherwise `false`

---------------------------------------------------------------------------------------------------
    org.companion.inquisitor.ConditionLessThan

Returns true if parameter1 < parameter2, otherwise false

Example Configuration
```
<Condition logic="less_than">
    <Function logic="get" param1="@{param1}" />
    <Function logic="get" param1="@{param2}" />
</Condition>
```
From this config, The result will be `true` if `@{param1} < @{param2}`
otherwise `false`

---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    org.companion.inquisitor.ConditionLessThanEquals
    
Returns true if parameter1 <= parameter2, otherwise false

Example Configuration
```
<Condition logic="less_than_or_equals">
    <Function logic="get" param1="@{param1}" />
    <Function logic="get" param1="@{param2}" />
</Condition>
```
From this config, The result will be `true` if `@{param1} <= @{param2}`
otherwise `false`

---------------------------------------------------------------------------------------------------
    org.companion.inquisitor.ConditionGreaterThan

Returns true if parameter1 > parameter2, otherwise false

Example Configuration
```
<Condition logic="greater_than">
    <Function logic="get" param1="@{age}" />
    <Function logic="get" param1="17" />
</Condition>
```
From this config, The result will be `true` if `@{age} > 17`
otherwise `false`

---------------------------------------------------------------------------------------------------
    org.companion.inquisitor.ConditionGreaterThanEquals

Returns true if parameter1 >= parameter2, otherwise false

Example Configuration
```
<Condition logic="greater_than_or_equals">
    <Function logic="get" param1="@{walletAmount}" />
    <Function logic="get" param1="@{productPrice}" />
</Condition>
```
From this config, The result will be `true` if `@{walletAmount} >= @{productPrice}`
otherwise `false`

---------------------------------------------------------------------------------------------------
    org.companion.inquisitor.ConditionIsNull

Returns true if parameter1 is null, otherwise false

Example Configuration
```
<Condition logic="is_null">
    <Function logic="get" param1="@{param1}" />
</Condition>
```
From this config, The result will be `true` if `@{param1} == null`
otherwise `false`

---------------------------------------------------------------------------------------------------
    org.companion.inquisitor.ConditionIsNotNull

Returns true if parameter1 is not null, otherwise false

Example Configuration
```
<Condition logic="is_not_null">
    <Function logic="get" param1="@{param1}" />
</Condition>
```
From this config, The result will be `true` if `@{param1} != null`
otherwise `false`

---------------------------------------------------------------------------------------------------
    org.companion.inquisitor.ConditionHasText

Returns true if parameter1 has text (length > 0), otherwise false

Example Configuration
```
<Condition logic="has_text">
    <Function logic="get" param1="@{param1}" />
</Condition>
```
From this config, The result will be `true` if `@{param1} != null && @{param1}.length > 0`
otherwise `false`

---------------------------------------------------------------------------------------------------
    org.companion.inquisitor.ConditionHasNoText

Returns true if parameter1 has no text (null or length = 0), otherwise false

Example Configuration
```
<Condition logic="has_no_text">
    <Function logic="get" param1="@{param1}" />
</Condition>
```
From this config, The result will be `true` if `@{param1} == null || @{param1}.length == 0`
otherwise `false`

---------------------------------------------------------------------------------------------------
    org.companion.inquisitor.ConditionIsLetter

Returns true the whole strings has only letter, otherwise false

Example Configuration
```
<Condition logic="is_letter">
    <Function logic="get" param1="@{param}" />
</Condition>
```
From this config, The result will be the result of `java.lang.Character.isLetter(@{param1})`

## License
This project is licensed under the Apache 2.0 License - see the [LICENSE](https://github.com/PlaySafe/impresario/blob/master/LICENSE) file for details