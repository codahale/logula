package com.codahale.logula.tests

import com.codahale.simplespec.Spec
import com.codahale.logula.SimpleFormatter
import org.apache.log4j.{Level, Logger}
import org.apache.log4j.spi.LoggingEvent
import org.junit.Test

class SimpleFormatterSpec extends Spec {
  class `Logula's formatter` {
    val formatter = new SimpleFormatter

    @Test def `formats entries as a single line` = {
      val event = new LoggingEvent("", Logger.getLogger("com.example.Yay"), 1270516531000L, Level.INFO, "That's a spicy pizza", null)

      formatter.format(event).must(be(
        "[I][2010-04-06 01:15:31,000] That's a spicy pizza\n"
      ))
    }
  }
}
