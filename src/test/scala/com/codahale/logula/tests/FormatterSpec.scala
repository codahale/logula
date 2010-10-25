package com.codahale.logula.tests

import com.codahale.simplespec.Spec
import com.codahale.logula.Formatter
import org.apache.log4j.{Level, Logger}
import org.apache.log4j.spi.LoggingEvent

object FormatterSpec extends Spec {
  class `Logula's formatter` {
    val formatter = new Formatter

    def `should handle exceptions itself` {
      formatter.ignoresThrowable must beFalse
    }

    def `should format entries as a single line` {
      val event = new LoggingEvent("", Logger.getLogger("com.example.Yay"), 1270516531000L, Level.INFO, "That's a spicy pizza", null)

      formatter.format(event) must beEqualTo(
        "INFO  [2010-04-06 01:15:31,000] com.example.Yay: That's a spicy pizza\n"
      )
    }

    def `should format exceptions as multiple lines` {
      val throwable = new IllegalArgumentException("augh")
      val event = new LoggingEvent("", Logger.getLogger("com.example.Yay"), 1270516531000L, Level.INFO, "That's a spicy pizza", throwable)

      val entry = formatter.format(event)

      entry must startWith("INFO  [2010-04-06 01:15:31,000] com.example.Yay: That's a spicy pizza\n")
      entry must include("! java.lang.IllegalArgumentException: augh")
    }
  }
}
