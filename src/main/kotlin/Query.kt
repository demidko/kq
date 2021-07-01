import com.fasterxml.jackson.databind.JsonNode

interface Query {
  fun apply(list: MutableList<JsonNode>, el: JsonNode)
  fun build(list: List<JsonNode>): List<Any>
}

interface Mapper : Query {

  override fun build(list: List<JsonNode>) = map(list)

  fun map(list: List<Any>): List<Any>
}

fun interface Mixer : Query {
  override fun build(list: List<JsonNode>) = list
}

fun map(mapper: JsonNode.() -> Any) = object : Mapper {

  override fun map(list: List<Any>) = (list as List<JsonNode>).map(mapper)

  override fun apply(list: MutableList<JsonNode>, el: JsonNode) {
    list.add(el)
  }
}

infix fun Mapper.map(mapper: Any.() -> Any) = object: Mapper {
  override fun apply(list: MutableList<JsonNode>, el: JsonNode) {
    TODO("Not yet implemented")
  }
}


{
  map(it).map(mapper)
}

infix fun Mixer.map(mapper: JsonNode.() -> Any) = Mapper {
  build(it as List<JsonNode>).map(mapper)
}

fun top(limit: Int) = Mixer { list, el ->
  if (list.size < limit) {
    list.add(el)
  }
}

infix fun Mixer.top(limit: Int) = Mixer { list, el ->
  apply(list, el)
  while (list.size > limit) {
    list.removeLast()
  }
}

infix fun Mapper.top(limit: Int) = Mapper { map(it).take(limit) }

fun where(filter: JsonNode.() -> Boolean) = Mixer { list, el ->
  if (el.filter()) {
    list.add(el)
  }
}

infix fun Mixer.where(filter: JsonNode.() -> Boolean) = Mixer { list, el ->
  if (el.filter()) {
    apply(list, el)
  }
}

infix fun Mapper.where(f: Any.() -> Boolean) = Mapper { it.filter(f) }

fun order(comparator: Comparator<JsonNode>) = Mixer { list, el ->
  list.add(el)
  list.sortWith(comparator)
}

infix fun Mixer.order(comparator: Comparator<JsonNode>) = Mixer { list, el ->
  apply(list, el)
  list.sortWith(comparator)
}

fun List<JsonNode>.collect(vararg queries: Query): List<List<Any>> {
  val results = queries.map { it to mutableListOf<JsonNode>() }
  for (node in this) {
    for ((query, result) in results) {
      query.apply(result, node)
    }
  }
  return results.map { (query, result) ->
    query.build(result)
  }
}