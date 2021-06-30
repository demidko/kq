import com.fasterxml.jackson.databind.JsonNode
import com.github.sisyphsu.dateparser.DateParserUtils
import java.util.Comparator.comparing

infix fun JsonNode.string(field: String) = get(field).asText()

infix fun JsonNode.time(field: String) = DateParserUtils.parseDateTime(string(field))

infix fun JsonNode.double(field: String) = get(field).asDouble()

infix fun JsonNode.long(field: String) = get(field).asLong()

infix fun JsonNode.int(field: String) = get(field).asInt()

infix fun JsonNode.bool(field: String) = get(field).asBoolean()

infix fun JsonNode.string(field: Int) = get(field).asText()

infix fun JsonNode.time(field: Int) = DateParserUtils.parseDateTime(string(field))

infix fun JsonNode.double(field: Int) = get(field).asDouble()

infix fun JsonNode.long(field: Int) = get(field).asLong()

infix fun JsonNode.int(field: Int) = get(field).asInt()

infix fun JsonNode.bool(field: Int) = get(field).asBoolean()

infix fun <T : Comparable<T>> Predicate.max(field: JsonNode.() -> T) = order(comparing(field))

infix fun <T : Comparable<T>> Action.max(field: JsonNode.() -> T) = order(comparing(field))

fun <T : Comparable<T>> max(field: JsonNode.() -> T) = order(comparing(field))

infix fun <T : Comparable<T>> Predicate.min(field: JsonNode.() -> T) = order(comparing(field).reversed())

infix fun <T : Comparable<T>> Action.min(field: JsonNode.() -> T) = order(comparing(field).reversed())

fun <T : Comparable<T>> min(field: JsonNode.() -> T) = order(comparing(field).reversed())