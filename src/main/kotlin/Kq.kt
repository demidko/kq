import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.github.sisyphsu.dateparser.DateParserUtils.parseDateTime
import javax.script.ScriptEngineManager

infix fun <T> Sequence<T>.where(predicate: T.() -> Boolean) = filter(predicate)
infix fun <T, R> Sequence<T>.select(transform: T.() -> R) = map(transform)
infix fun <T, R : Comparable<R>> Sequence<T>.min(selector: T.() -> R?) = sortedBy(selector)
infix fun <T, R : Comparable<R>> Sequence<T>.max(selector: T.() -> R?) = sortedByDescending(selector)
infix fun <T> Sequence<T>.top(n: Int) = take(n)
infix fun JsonNode.text(field: String) = get(field).asText()
infix fun JsonNode.text(field: Int) = get(field).asText()
infix fun JsonNode.time(field: String) = parseDateTime(text(field))
infix fun JsonNode.time(field: Int) = parseDateTime(text(field))
infix fun JsonNode.double(field: String) = get(field).asDouble()
infix fun JsonNode.double(field: Int) = get(field).asDouble()
infix fun JsonNode.long(field: String) = get(field).asLong()
infix fun JsonNode.long(field: Int) = get(field).asLong()
infix fun JsonNode.int(field: String) = get(field).asInt()
infix fun JsonNode.int(field: Int) = get(field).asInt()
infix fun JsonNode.bool(field: String) = get(field).asBoolean()
infix fun JsonNode.bool(field: Int) = get(field).asBoolean()
infix fun JsonNode.obj(field: Int) = get(field)
infix fun JsonNode.obj(field: String) = get(field)

val json = jsonMapper { addModule(JavaTimeModule()) }

fun String.eval(query: String) =
  ScriptEngineManager()
    .getEngineByExtension("kts")
    .eval(
      """ 
      import java.util.*  
      import java.time.*
      import java.time.Duration.*
      $this.useLines { (it.map(json::readTree) $query).forEach(::println)  }
      """
    )

fun main(args: Array<String>) {
  when (args.size) {
    1 -> "System.`in`.bufferedReader()".eval(args[0])
    2 -> """java.io.File("${args[0]}")""".eval(args[1])
    else -> println("Invalid params ${args.toList()}. Usage: kq [filename] [query]")
  }
}