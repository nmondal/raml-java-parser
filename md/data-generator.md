# RAML Data Generator
## Motivation 

Test data generation is a problem that is ailing the industry for decades. Unfortunately, there has been no headway into it. Some solutions are trivial - maintaining a large class of data - a golden test data source such as to speak - and some are just generating random data that has little bearing with the practicalities of the real world. 

There are some obvious good libraries, I found some of them because this is a problem I was trying to solve since last 10 years. My experience with high volume relational transactional monetary data made me realize that no test is better to test the system than a brute force approach of a large data attack onto the systems API. That I also worked in as the lead for system performances helped me formalising such things.

A curated list can be found in here:

1. Commercials ( products ) : https://www.guru99.com/test-data-generation-tools.html 
2. Fakers : https://github.com/DiUS/java-faker 
3. Mockeroo : https://www.mockaroo.com  
4. Easy Random : https://github.com/j-easy/easy-random 

The trouble with any of the existing system is a complete lack of type system in the data model. Types has their value, most notably in the help it provides while pressing the "." In case of object orientation is ascertained. In this case however, it defines the relationships between multiple types of data an enterprise will be using to run it's business. Unfortunately the trivial solutions provided does not work like that - it merely stores some very fixed structures like `email` or `state` which are essentially of limited applicability.

Also it is impossible to construct types and share and reuse them - an idea that `Easy Random` brushes upon. 

## Formalism 

Classically in CS this is a language generator - that is itself a grammar. Luckily for us, we have used the `RAML` type system itself - so the structure of an `object` so to speak is pretty understandable and can be recursively defined.

In short `RAML` type system produces the grammar required - and the data generator system generates objects based on the grammar.

`Atom` of such a grammar is primitive type - restricted to `RAML` spec for now :

1. Boolean 
2. Numeric 
   1. Integers
   2. Floating 
3. Date Time 
   1. Only Date 
   2. Only Time 
   3. Date Time 
4. String 
5. Algebric Types 
   1. Composites 
      1. Objects 
      2. Array 
   2. Union 

For now the distribution used is simple `Uniform` while other distributions provisions would be in place.

## Usage

Only supported form is `RAML 1.0`. 

### Configuration 

We need to start with a declaration of how a data type would look like, for example suppose we want a `Student` data type:

```yaml
#%RAML 1.0
title: Demo Types Generation 
mediaType: application/json
types:

  Student:
    type: object
    properties:
      id : string
      gender:
        enum : [ "M", "F", "U", "T" ]
      age: int8
      subjects: Subject[]

  Subject:
    type: object
    id : string
    name : string

/students/{studentId}:
  get:
    responses:
      200:
        body:
          application/json:
            type: Student

```



### Basic Code 

```java
// get model result 
RamlModelResult ramlModelResult = new RamlModelBuilder().buildApi(input);
// get API
Api api = ramlModelResult.getApiV10();
// now generated named type
Map<String,Object> stMap = TypeCreator.buildFrom(api, "Student");
```

The reason for using `Map<>` is to ensure mot tightly getting bound to any structural implantation. Different JVM languages treats record classes in different ways across versions - so the final user may chose how to convert it to a strict hardened type. 

