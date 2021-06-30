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

    val (topBySize, topByDuration) = ndjson.collect(
      where { !bool("muted") }
        top 3,
      max { long("size") }
        max { time("start") to time("finish") }
        top 5
    )

    assertThat(
      topBySize.size,
      equalTo(3)
    )
    assertThat(
      topBySize.all { !it.bool("muted") },
      equalTo(true)
    )
    assertThat(
      topByDuration.first().time("finish"),
      equalTo(parseDateTime("2021-06-24T14:08:08+10:00"))
    )
  }
}