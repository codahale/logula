package com.codahale.logula.tests

import org.scalatest.Spec
import org.scalatest.matchers.MustMatchers
import com.codahale.logula.{Formatter, Log, Logging}
import java.util.logging.{ConsoleHandler, Logger, Level}

class LoggingExample extends Logging {
  def getLog = log
}

class LoggingTest extends Spec with MustMatchers {
  describe("a class which extends Logging") {
    val example = new LoggingExample

    it("has a Log instance") {
      example.getLog.isInstanceOf[Log] must be(true)
    }
  }

  describe("configuring Logging") {
    Logging.configure(Level.WARNING, "com.codahale" -> Level.ALL)

    it("sets the root logger to the default level") {
      Logger.getLogger("").getLevel must be(Level.WARNING)
    }

    it("sets specific loggers to specific levels") {
      Logger.getLogger("com.codahale").getLevel must be(Level.ALL)
    }

    it("adds a ConsoleHandler with a Formatter to the root logger") {
      val root = Logger.getLogger("")
      root.getHandlers.map { _.getClass }.toList must equal(List(classOf[ConsoleHandler]))
      root.getHandlers.head.getFormatter.isInstanceOf[Formatter] must be(true)
    }
  }
}
