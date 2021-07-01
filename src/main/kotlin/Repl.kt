import javax.script.ScriptEngineManager

fun main(args: Array<String>) {

  check(args.size == 2) {
    "Invalid args. The utility only accepts file path and query"
  }

  val script = """
    java.io.File("${args.first()}")
      .ndjson()
      .collect(${args.last()})
      .println()
  """.trimIndent()

  ScriptEngineManager()
    .getEngineByExtension("kts")
    .eval(script)
}


