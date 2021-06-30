import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jsonMapper
import java.io.File

typealias Query = (MutableList<JsonNode>, JsonNode) -> Unit

class Predicate(val predicate: MutableList<JsonNode>.(JsonNode) -> Boolean) :
  Query by { list, el ->
    list.predicate(el)
  }

class Action(val action: MutableList<JsonNode>.(JsonNode) -> Unit) :
  Query by { list, el ->
    list.action(el)
  }

fun top(limit: Int) = Action {
  if (size < limit) {
    add(it)
  }
}

infix fun Predicate.top(limit: Int) = Action {
  if (size < limit && predicate(it)) {
    add(it)
  }
}

infix fun Action.top(limit: Int) = Action {
  action(it)
  while (size > limit) {
    removeLast()
  }
}

fun where(filter: JsonNode.() -> Boolean) = Action {
  if (it.filter()) {
    add(it)
  }
}

infix fun Action.where(filter: JsonNode.() -> Boolean) = Action {
  if (it.filter()) {
    action(it)
  }
}

infix fun Predicate.where(filter: JsonNode.() -> Boolean) = Action {
  if (predicate(it) && it.filter()) {
    add(it)
  }
}

fun order(comparator: Comparator<JsonNode>) = Action {
  add(it)
  sortWith(comparator)
}

infix fun Action.order(comparator: Comparator<JsonNode>) = Action {
  action(it)
  sortWith(comparator)
}

infix fun Predicate.order(comparator: Comparator<JsonNode>) = Action {
  if (predicate(it)) {
    add(it)
    sortWith(comparator)
  }
}

private val json = jsonMapper {
  addModule(JavaTimeModule())
}

fun File.execute(vararg queries: Query): List<MutableList<JsonNode>> {
  val results = queries.map { it to mutableListOf<JsonNode>() }
  forEachLine {
    val node = json.readTree(it)
    for ((query, result) in results) {
      query(result, node)
    }
  }
  return results.map { it.second }
}