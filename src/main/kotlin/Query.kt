import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jsonMapper
import java.io.File

typealias QueryType = (result: List<JsonNode>, node: JsonNode) -> Unit

class Query(q: QueryType) : QueryType by q

typealias MutableQueryType = (result: MutableList<JsonNode>, node: JsonNode) -> Unit

class MutableQuery(q: MutableQueryType) : MutableQuery by q

infix fun Query.top(limit: Int) = MutableQuery { result, node ->
  if(result.size < limit) {
    result.add(node)
  }
  this(result, node)
}

infix fun MutableQuery.top(limit: Int) = MutableQuery { result, node ->
  if(result.size > limit) {
    result.removeLast()
  }
}

fun top(size: Int) = MutableQuery { result, node ->
  if (result.size > size) {
    result.removeLast()
    true
  } else if (result.size < size) {
    result.add(node)
  } else {
    false
  }
}

infix fun Predicate.where(condition: JsonNode.() -> Boolean): Predicate = { result, node ->
  when (node.condition()) {
    true -> this(result, node)
    else -> false
  }
}

fun where(condition: JsonNode.() -> Boolean): Predicate = { result, node ->
  when (node.condition()) {
    true -> result.add(node)
    else -> false
  }
}

infix fun Predicate.order(comparator: Comparator<JsonNode>): Predicate = { result, node ->
  when (this(result, node)) {
    true -> {
      result.sortWith(comparator)
      true
    }
    else -> false
  }
}

fun order(comparator: Comparator<JsonNode>): Predicate = { result, node ->
  result.add(node)
  result.sortWith(comparator)
  true
}

private val json = jsonMapper {
  addModule(JavaTimeModule())
}

fun File.execute(vararg queries: Predicate): List<MutableList<JsonNode>> {
  val results = queries.map { it to mutableListOf<JsonNode>() }
  forEachLine {
    val node = json.readTree(it)
    for ((query, result) in results) {
      query(result, node)
    }
  }
  return results.map { it.second }
}