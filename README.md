# Kq

Modern cross-platform JSON processor with readable Kotlin syntax.

```shell
cat ~/Desktop/bdb.ndjson | kq '.filter{it.bool("muted")}.sortedBy{it.long("size")}.take(7)'
```

## Download

1. You need [jvm16](https://www.oracle.com/java/technologies/javase-jdk16-downloads.html) installed.
1. [Download release](https://github.com/demidko/kq/releases).

## Examples

```shell
java -jar kq file.ndsjon 'max {long("size")} top 3 where {bool("active")}'
cat file.ndjson | java -jar kq 'top 5 where{!bool("active")} min{int("some")}'
java -jar kq file.ndsjon 'top 5 min{between(time("first"), time("last"))}'
cat file.ndjson | java -jar kq 'where { obj("arr").int(0) > 5 }'
java -jar kq file.ndsjon 'where{!bool("broken")} top 3 min{ obj(4).obj("nested").bool("flag") }'
```

## Download with [Docker](https://www.docker.com/)

Start the container in current directory, for example, with `example.ndjson` file:

```shell
docker run -v `pwd`:`pwd` -w `pwd` -it --rm demidko/kq example.ndjson 'where{bool("active")} max{long("size")} top 10'
```

## Documentation

Any request contains control constructs and inline expressions.

### Constructs

You can use any Kotlin sequence extension and following infix extensions in any order:

```kotlin
/* Sequence containing only elements matching the given predicate expression. */
where { /* expression */ }

/* Sequence sorted according to natural sort order of the value returned by specified selector expression. */
min { /* expression */ }

/* Sequence sorted descending according to natural sort order of the value returned by specified selector expression. */
max { /* expression */ }

/* Sequence containing first n elements. */
top(n)

/* Sequence containing the results of applying the given transform expression to each element in the original sequence */
select { /* expression */ }
```

### Expressions

You can use any Kotlin expression and following functions:

```kotlin
/* Subnode of current json node */
obj(name: String)
obj(idx: Int)

/* Logic value of current json node */
bool(name: String)
bool(idx: Int)

/* Integer number of current json node */
int(name: String)
int(idx: Int)

/* Double  number of current json node */
double(name: String)
double(idx: Int)

/* Text value of current json node */
text(name: String)
text(idx: Int)

/* LocalDateTime value of current json node */
time(name: String)
time(idx: Int)
```

## Build

### Build Unix command

You need [jvm16](https://www.oracle.com/java/technologies/javase-jdk16-downloads.html) installed.
Run `./install.sh`. Now you can run command, for example:

```shell
cat example.ndjson | kq 'where{bool("active")} top 10'
```

### Build [Java](https://www.oracle.com/java/technologies/javase-jdk16-downloads.html) utility

Execute command:

```shell
./gradlew clean build
``` 

Your jar will be located at `./build/libs` with `-all.jar` postfix. Now you can run jar, for
example:

```shell
cat example.ndjson | java -jar kq-all.jar 'where{bool("active")} top 10'
```

### Build [Docker](https://www.docker.com/) image

Execute command

```shell
docker build . -t kq
```

Now you can run container, for example:

```shell
docker run -v `pwd`:`pwd` -w `pwd` -it --rm kq example.ndjson 'where{bool("active")} top 10'
```

### Build cross-platform utility with [GraalVM](https://www.graalvm.org/reference-manual/native-image/#install-native-image)

Execute following commands:

```shell
./gradlew clean build
native-image --allow-incomplete-classpath -jar ./build/libs/*-all.jar
``` 

Your native utility without runtime dependencies will be located at current directory with `-all`
postfix. Now you can run utility, for example:

```shell
cat example.ndjson | ./kq-all 'where{bool("active")} top 10'
```

## Roadmap

1. jsr223 -> KEEP
2. KEEP -> native-image


