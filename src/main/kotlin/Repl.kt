import com.fasterxml.jackson.databind.JsonNode
import java.io.File
import java.lang.System.`in`
import javax.script.ScriptEngineManager

fun main(args: Array<String>) {
  check(args.size == 1) { "Invalid args. Еhe utility only accepts one file path." }
  val ndjson = File(args.first())
  check(ndjson.exists()) { "$ndjson not found" }
  with(ScriptEngineManager().getEngineByExtension("kts")) {
    print(":) ")
    `in`.reader().forEachLine {
      try {
        for (batch in eval("ndjson.execute($it)") as List<List<JsonNode>>) {
          batch.forEach(::println)
          println()
        }
      } catch (e: RuntimeException) {
        println(e)
        println("See https://github.com/demidko/analyze")
      }
      print(":) ")
    }
  }
}
