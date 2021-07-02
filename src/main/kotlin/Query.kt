import com.fasterxml.jackson.databind.JsonNode

typealias FieldGetter<T> = () -> T

fun <A, B> FieldGetter<A>.bind(mapper: A.() -> B): FieldGetter<B> = { this().mapper() }

typealias AnyFieldGetter = FieldGetter<Any>

typealias NodeFieldGetter = FieldGetter<JsonNode>

fun interface Query {

  fun execute(result: MutableList<Any>, field: AnyFieldGetter)
}

fun interface AnyQuery : Query

infix fun AnyQuery.top(limit: Int) = AnyQuery { result, any ->
  execute(result, any)
  while (result.size > limit) {
    result.removeLast()
  }
}

infix fun AnyQuery.where(filter: Any.() -> Boolean) = AnyQuery { result, any ->
  if (any().filter()) {
    execute(result, any)
  }
}

infix fun AnyQuery.order(comparator: Comparator<Any>) = AnyQuery { result, any ->
  execute(result, any)
  result.sortWith(comparator)
}

fun map(mapper: JsonNode.() -> Any) = AnyQuery { result, any ->
  result.add((any() as JsonNode).mapper())
}

infix fun AnyQuery.map(mapper: Any.() -> Any) = AnyQuery { result, any ->
  execute(result, any)
  result.replaceAll(mapper)
}

fun interface JsonQuery : Query {

  override fun execute(result: MutableList<Any>, field: AnyFieldGetter) {
    query(result as MutableList<JsonNode>, field as NodeFieldGetter)
  }

  fun query(result: MutableList<JsonNode>, filed: NodeFieldGetter)
}

fun top(limit: Int) = JsonQuery { result, any ->
  if (result.size < limit) {
    result.add(any())
  }
}

infix fun JsonQuery.top(limit: Int) = JsonQuery { result, field ->
  query(result, field)
  while (result.size > limit) {
    result.removeLast()
  }
}

fun where(filter: JsonNode.() -> Boolean) = JsonQuery { result, field ->
  if (field().filter()) {
    result.add(field())
  }
}

infix fun JsonQuery.where(filter: JsonNode.() -> Boolean) = JsonQuery { result, field ->
  if (field().filter()) {
    query(result, field)
  }
}

fun order(comparator: Comparator<JsonNode>) = JsonQuery { result, field ->
  result.add(field())
  result.sortWith(comparator)
}

infix fun JsonQuery.order(comparator: Comparator<JsonNode>) = JsonQuery { result, filed ->
  query(result, filed)
  result.sortWith(comparator)
}

fun json(mapper: JsonNode.() -> JsonNode) = JsonQuery { result, node ->
  result.add(node().mapper())
}

infix fun JsonQuery.json(mapper: JsonNode.() -> JsonNode) = JsonQuery { result, node ->
  query(result, node)
  result.replaceAll(mapper)
}

infix fun JsonQuery.map(mapper: JsonNode.() -> Any) = AnyQuery { result, any ->
  query(result as MutableList<JsonNode>, any.bind(mapper))
  (result as MutableList<Any>).replaceAll {
    when (it is JsonNode) {
      true -> it.mapper()
      else -> it
    }
  }
}

fun List<JsonNode>.collect(vararg queries: Query): List<List<Any>> {
  val results = queries.map { it to mutableListOf<Any>() }
  for (node in this) {
    for ((q, result) in results) {
      q.execute(result, node)
    }
  }
  return results.map { (_, result) -> result }
}