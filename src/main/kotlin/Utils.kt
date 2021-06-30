import com.fasterxml.jackson.databind.JsonNode
import com.github.sisyphsu.dateparser.DateParserUtils
import java.util.Comparator.comparing

fun JsonNode.string(field: String) = get(field).asText()

fun JsonNode.time(field: String) = DateParserUtils.parseDateTime(string(field))

fun JsonNode.double(field: String) = get(field).asDouble()

fun JsonNode.long(field: String) = get(field).asLong()

fun JsonNode.int(field: String) = get(field).asInt()

fun JsonNode.bool(field: String) = get(field).asBoolean()


infix fun <T : Comparable<T>> Predicate.max(field: JsonNode.() -> T) = order(comparing(field))

infix fun <T : Comparable<T>> Action.max(field: JsonNode.() -> T) = order(comparing(field))

fun <T : Comparable<T>> max(field: JsonNode.() -> T) = order(comparing(field))

infix fun <T : Comparable<T>> Predicate.min(field: JsonNode.() -> T) = order(comparing(field).reversed())

infix fun <T : Comparable<T>> Action.min(field: JsonNode.() -> T) = order(comparing(field).reversed())

fun <T : Comparable<T>> min(field: JsonNode.() -> T) = order(comparing(field).reversed())
