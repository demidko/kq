import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.github.sisyphsu.dateparser.DateParserUtils.parseDateTime
import java.io.File
import javax.script.ScriptEngineManager

inline infix fun <T> Iterable<T>.where(crossinline predicate: T.() -> Boolean) = filter(predicate)
inline infix fun <T, R> Iterable<T>.select(crossinline transform: T.() -> R) = map(transform)
inline infix fun <T, R : Comparable<R>> Iterable<T>.min(crossinline selector: T.() -> R?) = sortedBy(selector)
inline infix fun <T, R : Comparable<R>> Iterable<T>.max(crossinline selector: T.() -> R?) = sortedByDescending(selector)
infix fun <T> Iterable<T>.top(n: Int) = take(n)
infix fun JsonNode.string(field: String) = get(field).asText()
infix fun JsonNode.time(field: String) = parseDateTime(string(field))
infix fun JsonNode.double(field: String) = get(field).asDouble()
infix fun JsonNode.long(field: String) = get(field).asLong()
infix fun JsonNode.int(field: String) = get(field).asInt()
infix fun JsonNode.bool(field: String) = get(field).asBoolean()
infix fun JsonNode.string(field: Int) = get(field).asText()
infix fun JsonNode.obj(field: Int) = get(field)
infix fun JsonNode.obj(field: String) = get(field)
infix fun JsonNode.time(field: Int) = parseDateTime(string(field))
infix fun JsonNode.double(field: Int) = get(field).asDouble()
infix fun JsonNode.long(field: Int) = get(field).asLong()
infix fun JsonNode.int(field: Int) = get(field).asInt()
infix fun JsonNode.bool(field: Int) = get(field).asBoolean()

val json = jsonMapper { addModule(JavaTimeModule()) }

fun File.ndjson(): List<JsonNode> = when (isDirectory) {
  true -> listFiles { it: File -> it.extension == "ndjson" || it.extension == "json" }
    ?.flatMap(File::ndjson)
    ?: emptyList()
  else -> useLines {
    it.map(json::readTree).toList()
  }
}

fun main(args: Array<String>) {
  check(args.size == 2) { "Invalid params. Right usage: [path] [query]" }
  ScriptEngineManager()
    .getEngineByExtension("kts")
    .eval(
      """
      import java.time.*
      import java.time.Duration.between
      (java.io.File("${args.first()}").ndjson() ${args.last()}).forEach(::prinltn)
      """
    )
}