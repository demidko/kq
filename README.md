# Analyze

Command-line JSON processor with Kotlin dsl.

## Usage

You need [jdk16](https://www.oracle.com/java/technologies/javase-jdk16-downloads.html) installed.  
Select your ndjson:

```shell
java -jar analyze file.ndjson
```

Enter any queries, for example:

```kotlin
:) max { long("size") } top 3 where { bool("active") }, top 5 where { !bool("active") }
:) top 3 
```

## Build

Execute `./gradlew clean build`. Your jar will be located at `./build/libs`.


