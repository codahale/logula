package com.codahale.logula.tests

import com.codahale.simplespec.Spec
import com.codahale.logula.Formatter
import org.apache.log4j.{Level, Logger}
import org.apache.log4j.spi.LoggingEvent
import org.junit.Test

class FormatterSpec extends Spec {
  class `Logula's formatter` {
    val formatter = new Formatter

    @Test def `handles exceptions itself` = {
      formatter.ignoresThrowable.must(be(false))
    }

    @Test def `formats entries as a single line` = {
      val event = new LoggingEvent("", Logger.getLogger("com.example.Yay"), 1270516531000L, Level.INFO, "That's a spicy pizza", null)

      formatter.format(event).must(be(
        "INFO  [2010-04-06 01:15:31,000] com.example.Yay: That's a spicy pizza\n"
      ))
    }

    @Test def `formats exceptions as multiple lines` = {
      val throwable = new IllegalArgumentException("augh")
      val event = new LoggingEvent("", Logger.getLogger("com.example.Yay"), 1270516531000L, Level.INFO, "That's a spicy pizza", throwable)

      val entry = formatter.format(event)

      entry.startsWith("INFO  [2010-04-06 01:15:31,000] com.example.Yay: That's a spicy pizza\n").must(be(true))
      entry.contains("! java.lang.IllegalArgumentException: augh").must(be(true))
    }
  }
}
