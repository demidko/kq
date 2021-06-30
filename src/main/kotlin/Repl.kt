import com.fasterxml.jackson.databind.JsonNode
import java.lang.Thread.currentThread
import javax.script.ScriptEngineManager
import javax.script.ScriptException

fun main(args: Array<String>) {

  check(args.size == 1) {
    "Invalid args. The utility only accepts one file path."
  }

  val kotlin = ScriptEngineManager().getEngineByExtension("kts").apply {
    eval("val cache = java.io.File(\"${args.first()}\").ndjson()")
  }

  fun execute(q: String) =
    (kotlin.eval("cache.collect($q)") as List<List<JsonNode>>).apply {
      println()
      forEach {
        it.forEach(::println)
        println()
      }
    }


  while (!currentThread().isInterrupted) {
    print("kts :) ")
    try {
      when (val q = readLine()) {
        null, ":q" -> return
        else -> execute(q)
      }
    } catch (e: RuntimeException) {
      println(e)
    } catch (e: ScriptException) {
      println(e)
    }
  }
}


