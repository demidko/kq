# Analyze

Modern cross-platform JSON processor with readable Kotlin syntax.

## Usage with Java

You need java installed. You need [utility](https://github.com/demidko/analyze/releases) installed.
For example with `example.ndjson` file

```shell
java -jar analyze example.ndjson 'where{long("size") > 10} max{long("size")} select{int("id")}'
```

or on Unix

```shell
cat example.ndjson | analyze 'where{long("size") > 10} max{long("size")} select{int("id")}'
```

## Usage with [Docker](https://www.docker.com/)

Start the container in current directory, for example, with `example.ndjson` file:

```shell
docker run -v `pwd`:`pwd` -w `pwd` -it --rm demidko/analyze example.ndjson 'where{bool("active")} max{long("size")} top 10'
```

## Documentation

Any request contains control constructs and inline expressions.

### Constructs

You can combine query's control constructs in any order:

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

### Examples

```shell
analyze file.ndsjon 'max {long("size")} top 3 where {bool("active")}'

cat file.ndjson | analyze 'top 5 where{!bool("active")} min{int("some")}'

analyze file.ndsjon 'top 5 min{between(time("first"), time("last"))}'

cat file.ndjson | analyze 'where { obj("arr").int(0) > 5 }'

analyze file.ndsjon 'where{!bool("broken")} top 3 min{ obj(4).obj("nested").bool("flag") }'
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
cat example.ndjson | java -jar analyze 'where{bool("active")} top 10'
```

### Build Unix [Java](https://www.oracle.com/java/technologies/javase-jdk16-downloads.html) utility

Execute following commands:

```shell
./gradlew clean build
mv build/libs/*-all.jar /usr/local/bin/analyze.jar
echo "java -jar /usr/local/bin/analyze.jar \$@" > /usr/local/bin/analyze
chmod a+x /usr/local/bin/analyze
```

Now you can run utility with alias, for example:

```shell
cat example.ndjson | analyze 'where{bool("active")} top 10'
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
cat example.ndjson | ./analyze 'where{bool("active")} top 10'
```

### Build [Docker](https://www.docker.com/) image

Execute command

```shell
docker build . -t analyze
```

Now you can run container, for example:

```shell
docker run -v `pwd`:`pwd` -w `pwd` -it --rm analyze example.ndjson 'where{bool("active")} top 10'
```



