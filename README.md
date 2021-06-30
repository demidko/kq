# Analyze

Modern JSON processor with readable syntax.

## Usage

Start the container with the desired directory:

```shell
docker run -v desired/dir:/opt -it demidko/analyze
```

To start analysis select your ndjson:

```shell
analyze file.ndjson
```

Then enter any queries in any order, separated by commas, for example:

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

## Documentation

All queries contain control constructs and expressions. You can combine queries separated by commas
in any order.

### Control constructs

You can combine query's control constructs in any order:

```kotlin
where { /* logic expression */ }
order { /* expression for comparator */ }
min { /* expression for comparator key */ }
max { /* expression for reversed comparator key */ }
top(limit: Int)
```

### Expressions helpers

Inside the expressions, you can use Kotlin with json helpers.

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



