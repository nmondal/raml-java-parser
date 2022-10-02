# RAML Java Parser ( Part of Raml-Utils)
This is a Java implementation of a [RAML](http://raml.org) parser for versions [1.0](http://raml.org/raml-10-spec) and [0.8](http://raml.org/raml-08-spec).
The parser depends on SnakeYaml, a Java YAML parser.

See http://raml.org for more information about RAML.

As the original parser was deprecated - and with lots of security vulnerabilities I decided to maintain it. Should not be much. Moreover, it was crucial for my musings with data generation frameworks. Any data generation dreamworlds requires an abstract type system and after long inspection I found out `RAML` is better than that of `Open API`.

As of now it is available as snapshot to maven repository.   


## Maven

```xml
  <dependency>
    <groupId>org.zoomba-lang</groupId>
    <artifactId>raml-parser-2</artifactId>
    <version>${raml-parser-version}</version>
  </dependency>
```

### Development version

As of now this is the only version available which is security risk free - and in snapshot repository. I would possibly try to fix some bugs in the parser - but that is not the high priority. 

## Build

### JAR file without dependencies

```mvn clean package```

### JAR file with dependencies

```mvn clean package -P jar-with-dependencies```

**Run standalone validator**

```java -jar raml-parser-2-{version}.jar raml-file ...```

### Raml Java Parser JVM Arguments
In order to provide more flexibility, users can set different system properties when parsing different RAML files. Here we list all the system properties you can use right now:

Argument | Description | Default Value
-------- | ----------- | -------------
```yagi.json_duplicate_keys_detection``` | Setting it to true will make the parser fail if any JSON example contains duplicated keys | ```true```
```raml.json_schema.fail_on_warning``` | Setting it to true will make the parser fail if any example validated against a particular Json Schema throws a warning message | ```false```
```yagi.date_only_four_digits_year_length_validation```|	If TRUE, years of more than 4 digits are considered invalid | ```true```
```org.raml.date_only_four_digits_year_length_validation```|	Same as "yagi.date_only_four_digits_year_length_validation" (kept for backwards compatibility)| ```true```
```org.raml.dates_rfc3339_validation```|	if TRUE, enables RFC3339 validation for "datetime" type| ```true```
```org.raml.dates_rfc2616_validation```|	if TRUE, enables RFC2616 validation for "datetime" type| ```true```
```raml.xml.expandExternalEntities```|	Controls Java's EXTERNAL_GENERAL_ENTITIES_FEATURE and EXTERNAL_PARAMETER_ENTITIES_FEATURE| ```false```
```raml.xml.expandInternalEntities```|	Controls Java's DISALLOW_DOCTYPE_DECL_FEATURE| ```false```
```org.raml.strict_booleans```|	If FALSE, the strings "true" and "false" are valid for boolean type	| ```false```
```org.raml.fallback_datetime_to_datetime-only```|	if TRUE, value passed to a datetime type will fallback on the datetime-only type and validate accordingly| ```false```
```org.raml.cast_strings_as_numbers```|	if TRUE, will attempt to cast strings as numbers and validate| ```false```
```org.raml.nillable_types```|	if TRUE, makes all types equivalent to type: <code>type: type&#124; nil;</code> | ```false```
```raml.verifyRaml```|Verify the RAML file for YAML reference abuses | `true`
```raml.verifyReferenceCycle```|Specifically verify YAML reference cycles| `true`
```raml.maxDepth```|Limit depth of YAML references | `2000`
```raml.maxReferences```|Limit number of YAML references in expansions|`10000`
```raml.parser.encoding```|	Defines the charset being used by the parser| ```UTF-8```

The RAML parser's XML parsing components also respect Java XML entity properties.

## Usage

```java
RamlModelResult ramlModelResult = new RamlModelBuilder().buildApi(input);
if (ramlModelResult.hasErrors())
{
    for (ValidationResult validationResult : ramlModelResult.getValidationResults())
    {
        System.out.println(validationResult.getMessage());
    }
}
else
{
    Api api = ramlModelResult.getApiV10();

}
```

## Contribution guidelines

### Contributor’s Agreement

No worries, raise PR. 

### Pull requests are always welcome

I have decided that the code in open source is a terrible problem, so I would personally figure out if a PR is done in the right way.

### Create issues...

Freely. No problem at all. Love to fix issues or would agree it is a troublesome one. I am only retired, not defunct. 

### ...but check for existing issues first!

Please take a moment to check that an issue doesn't already exist documenting your bug report or improvement proposal. If it does, it never hurts to thumb up the original post or add "I have this problem too". This will help prioritize the most common problems and requests.

### Merge approval

I will review your pull request and will merge into the main repo. Commits get approval based on the conventions outlined in the previous section. For example, new features without additional tests will be not approved.
