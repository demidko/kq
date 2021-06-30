# Analyze

Command-line JSON processor with Kotlin dsl.

## Usage example

You need [jdk16](https://www.oracle.com/java/technologies/javase-jdk16-downloads.html) installed.

1. Select your ndjson:

```shell
java -jar analyze file.ndjson
```

2. Enter any queries in any order, separated by commas, for example:

```kotlin
max { long("size") }
top 3
where { bool("active") },

top(5)
where { !bool("active") }
min { int("some") },

top 5
min { time("first") to time("last") },

where { get("arr") int (0) > 5 },

where { !get("broken") }
top 3
min { get(4) get ("nested") bool ("flag") }
```

Enjoy results!

## Language documentation

1. You can combine query's control constructs in any order:

```kotlin
where { /* logic expression */ }
order { /* expression for comparator */ }
min { /* expression for comparator key */ }
max { /* expression for reversed comparator key */ }
top(limit: Int)
```

2. Inside the expressions, you can use Kotlin with json helpers.

```kotlin
get(name: String)
get(idx: String)
bool(name: String)
bool(idx: String)
int(name: String)
int(idx: String)
double(name: String)
double(idx: String)
string(name: String)
string(idx: String)
time(name: String)
time(idx: String)
```

3. You can combine queries separated by commas in any order.

## Download with Docker

Start the container with the desired directory:

```shell
docker run -v desired_dir:/opt -it demidko/analyze
```

Then start analysis:

```shell
analyze your_host_file.ndjson
```



