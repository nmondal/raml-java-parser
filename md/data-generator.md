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

Only supported form is `RAML 1.0`. For more overview about basal `RAML` data types - please see the [RAML 1.0 spec](https://github.com/raml-org/raml-spec/blob/master/versions/raml-10/raml-10.md#raml-data-types).

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

## Manual 

### Primitives 

#### Boolean 

```yaml
SomeBoolean:
    type: boolean
```

As of now the distribution used is `Uniform`. 

#### String

```yaml
Phone:
  type: string
  pattern: "(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]\\d{3}[\\s.-]\\d{4}" # note the use of regex 

Email:
    type: string
    pattern: "[a-z]+@[a-z]+\\.com"
    minLength: 15 # min no of chars 
    maxLength: 32 # max no of chars 

```



#### Numerics

##### Integers

```yaml
  AgeHuman:
    type : integer
    minimum: 18
    maximum: 80
    format: int8

  AgeBuilding:
    type : integer
    minimum: 0
    maximum: 20000
    format: int16

  AgeFossil:
    type : integer # we could have used int32 too.
    minimum: 0
    maximum: 1000000
    format: int32

  AgeExistence:
    type : integer
    minimum: 0
    maximum: 1000000000
    format: int64

```

The default of Course is set to be `int32`. Now, here is a problem I do not like byte spec in a datatype, that is antithesis to a declarative paradigm - integer should be an integer - and can be arbitrary long. But the `RAML` was not created in the same spirit it was designed to create platform independent data types for transport. 

##### Fractions

```yaml
FuzzyFloat:
  type: number
  format: float
   minimum: 0.00
   maximum: 1.00

Fuzziness:
  type: number
  format: double
  minimum: 0.0001
  maximum: 1.000

```

Type `number` defaults into `double`. 

#### Date & Time 

```yaml
BirthDay:
  type : date-only

LunchTime:
  type: time-only

BirthDateTime:
   type: datetime-only
```

Now here `RAML` is a bit deficient - it does not specify the bounds of the fields, and thus  we have created the following  hack:

```java
DateTimeCreator.minDateTimeStamp = <put here> ; // set whatver 
DateTimeCreator.maxDateTimeStamp = <put here> ; // set whatver
```

Once you put both limits, all date time would be within these two limits. Efforts are underway to fix it based on instances. 

#### Enums

##### Numerics 

```yaml
Directions:
  type: integer
  enum : [0,1,2,3]
```

Albeit coming as integer types - but picks one of the listed items. 

##### Strings 

```yaml
Directions:
  type: string
  enum : ["North", "East", "West", "South"]
```



### Compositions

#### Containers : Array

```yaml
Email:
  type: string
  pattern: "[a-z]+@[a-z]+\\.com"
  minLength: 15
  maxLength: 32
Emails:
  type: Email[]
  minItems: 1
  maxItems: 5
```

This ensures `Type[]` is declared as array of Type. 

#### Union 

```yaml
Email:
  type: string 
Phone:
	type: integer
	minimum: 10000000
	maximum: 99999999

Contact:
    type: Email | Phone
```

#### Composites 

We started with `Student` object itself, let's try to explain it again here:

```yaml
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
```

As one can see we can directly access a type or can refer a type.  We do support recursion. In that case, a specific extra property defines the `recursion` depth:

```yaml
Person:
	type: object
	properties:
		id : string
		partner? : Person
		::depth:
			type: integer
			minimum: 1
			maximum: 1
		gender:
			enum : [ "M", "F", "U", "T" ]

```

