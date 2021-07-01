# Analyze

Modern JSON processor with readable syntax.

## Usage

You need [Docker](https://www.docker.com/) installed. Start the container with the desired directory
and file, for example, `~/Desktop` directory and `example.ndjson` file:

```shell
docker run -v ~/Desktop:/opt -it demidko/analyze example.ndjson
```

Then enter any queries in any order, separated by commas, for example:

```kotlin
max { long("size") } top 3 where { bool("active") },

top(5) where { !bool("active") } min { int("some") },

top 5 min { time("first") to time("last") },

where { get("arr") int (0) > 5 },

where { !get("broken") } top 3 min { get(4) get ("nested") bool ("flag") }
```

Enjoy results! Use `:q` to exit.

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
get(name: String) // get node
get(idx: Int) // get node
bool(name: String)
bool(idx: Int)
int(name: String)
int(idx: Int)
double(name: String)
double(idx: Int)
string(name: String)
string(idx: Int)
time(name: String)
time(idx: Int)
```



