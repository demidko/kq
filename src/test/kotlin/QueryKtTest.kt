import com.github.sisyphsu.dateparser.DateParserUtils.parseDateTime
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test
import java.io.File
import java.time.Duration.between


class QueryKtTest {

  @Test
  fun analyze() {
    val ndjson = javaClass
      .getResource("/example.ndjson")!!
      .toURI()
      .let(::File)
    val (topBySize, topByDuration) = ndjson.execute(
      where { !bool("muted") }
        max { long("size") }
        top  3,
      max { between(time("start"), time("finish")) }
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
      equalTo(parseDateTime("2021-06-24T14:10:52"))
    )
  }
}