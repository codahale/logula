package com.codahale.logula.tests

import org.scalatest.matchers.MustMatchers
import com.codahale.logula.Formatter
import org.scalatest.Spec
import java.util.logging.{Level, LogRecord}

class FormatterTest extends Spec with MustMatchers {
  describe("formatting a log record") {
    val formatter = new Formatter
    val record = new LogRecord(Level.INFO, "That's a spicy pizza")
    record.setLevel(Level.INFO)
    record.setMillis(1270516531000L)
    record.setSourceClassName("MyClass")
    record.setSourceMethodName("doAThing")

    it("produces a single-line entry") {
      val entry = formatter.format(record)

      entry must equal("INFO    [2010-04-06 01:15:31.000] MyClass#doAThing: That's a spicy pizza\n")
    }
  }

  describe("formatting a log record with an exception") {
    val formatter = new Formatter
    val record = new LogRecord(Level.INFO, "That's a spicy pizza")
    val throwable = new IllegalArgumentException("augh")
    record.setLevel(Level.INFO)
    record.setMillis(1270516531000L)
    record.setSourceClassName("MyClass")
    record.setSourceMethodName("doAThing")
    record.setThrown(throwable)

    it("produces a single-line entry with a marked stack trace") {
      val entry = formatter.format(record)

      entry must startWith("INFO    [2010-04-06 01:15:31.000] MyClass#doAThing: That's a spicy pizza\n")
      entry.contains("! java.lang.IllegalArgumentException: augh") must be(true)
    }
  }
}
