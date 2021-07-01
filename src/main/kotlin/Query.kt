typealias Query = MutableList<Any>.(Any) -> Unit

fun top(limit: Int): Query = {
  if (size < limit) {
    add(it)
  }
}

fun Query.top(limit: Int): Query = {
  while (size > limit) {
    removeLast()
  }
}

fun where(filter: Any.() -> Boolean): Query = {
  if (it.filter()) {
    add(it)
  }
}

fun Query.where(filter: Any.() -> Boolean): Query = {
  if (it.filter()) {
    this@where(it)
  }
}

fun order(comparator: Comparator<Any>): Query = {
  add(it)
  sortWith(comparator)
}

fun Query.order(comparator: Comparator<Any>): Query = {
  this@order(it)
  sortWith(comparator)
}

fun map(mapper: Any.() -> Any): Query = {
  add(it.mapper())
}

fun Query.map(mapper: Any.() -> Any): Query = {
  this@map(it)
  this.replaceAll(mapper)
}

