import com.fasterxml.jackson.databind.JsonNode
import com.github.sisyphsu.dateparser.DateParserUtils.parseDateTime
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import java.io.File


class QueryKtTest {

  @Test
  fun analyze() {
    val ndjson = javaClass
      .getResource("/example.ndjson")!!
      .toURI()
      .let(::File)
      .let(File::ndjson)

    val (topBySize, topByDuration, tsv) = ndjson.collect(

      where { !bool("muted") }
        top 3,

      max { long("size") }
        max { time("start") to time("finish") }
        top 5,

      where { long("size") < 10 } map { "${get("size")}\t${get("id")}" } top 3
    )

    tsv.prinltn()

    assertThat(
      topBySize.size,
      equalTo(3)
    )
    assertThat(
      topBySize.all { !(it as JsonNode).bool("muted") },
      equalTo(true)
    )
    assertThat(
      (topByDuration.first() as JsonNode).time("finish"),
      equalTo(parseDateTime("2021-06-24T14:08:08+10:00"))
    )
  }
}