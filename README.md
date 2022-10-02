# RAML Java Utils

This has the following projects:

1. A Java implementation of a [RAML](http://raml.org) parser for versions [1.0](http://raml.org/raml-10-spec) and [0.8](http://raml.org/raml-08-spec).
   The parser depends on SnakeYaml, a Java YAML parser. Upto date with latest dependencies and 0 security vulnerabilities.
2. A Synthetic data generator using RAML's pretty excellent type system.

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

## Usage 

As of now the document has been split into two halves.

1. RAML Parser proper - the document for use can be found in here : [Raml Parser](md/raml-parser-2.md)
2. RAML Type based Synthetic Data Generator -  [DART](md/data-generator.md)

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
