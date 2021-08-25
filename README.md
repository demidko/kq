# Kq

Modern Docker JSON processor with readable Kotlin syntax.

## Usage

You need Docker installed:

```shell
 docker run -v `pwd`:`pwd` -w `pwd` -it --rm demidko/kq bdb.ndjson \
 'filter{ it.bool("muted") && between(it.time("firstActivity"), it.time("lastActivity")).toHours() >= 5 }.sumOf{ it.long("bytesCount") }.forEach(::println)'
```

## Documentation

You can use
any [Kotlin sequence extensions](https://kotlinlang.org/docs/sequences.html#sequence-operations)
with additional json node expressions:

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

### Build [Java](https://www.oracle.com/java/technologies/javase-jdk16-downloads.html) utility

Execute command:

```shell
./gradlew clean build
``` 

Your jar will be located at `./build/libs` with `-all.jar` postfix. Now you can run jar, for
example:

```shell
cat example.ndjson | java -jar kq-all.jar 'where{it.bool("active")}.take(10).forEach(::println)'
```

### Build [Docker](https://www.docker.com/) image

Execute command

```shell
docker build . -t kq
```

Now you can run container, for example:

```shell
docker run -v `pwd`:`pwd` -w `pwd` -it --rm kq example.ndjson 'filter{it.bool("active")}.take(10).forEach(::println)'
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
cat example.ndjson | ./kq-all 'filter{bool("active")}.take(10).forEach(::println)'
```

## Roadmap

1. jsr223 -> KEEP
2. KEEP -> native-image


