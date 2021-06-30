import com.fasterxml.jackson.databind.JsonNode
import java.io.File
import java.lang.System.`in`
import javax.script.ScriptEngineManager

fun main(args: Array<String>) {
  check(args.size == 1) { "Invalid args. The utility only accepts one file path." }
  val ndjson = File(args.first()).ndjson()
  with(ScriptEngineManager().getEngineByExtension("kts")) {
    print(":) ")
    `in`.reader().forEachLine {
      try {
        println()
        for (batch in eval("ndjson.collect($it)") as List<List<JsonNode>>) {
          batch.forEach(::println)
          println()
        }
      } catch (e: RuntimeException) {
        println(e)
      }
      print(":) ")
    }
  }
}


