package com.codahale.logula.tests

import com.codahale.simplespec.Spec
import org.apache.log4j.{Logger, Level}
import com.codahale.logula.{Log, Logging}

class LoggingExample extends Logging {
  def getLog = log
}

object LoggingSpec extends Spec {
  class `A class which extends Logging` {
    val example = new LoggingExample

    def `should have a Log instance` {
      example.getLog must haveClass[Log]
    }
  }

  class `Configuring logging` {
    try {
      Logging.logToConsole(Level.WARN, "com.codahale" -> Level.ALL)
    } catch {
      case e =>
        e.printStackTrace()
        throw e
    }

    def `should set the root logger level to the default level` {
      Logger.getRootLogger.getLevel must be(Level.WARN)
    }

    def `should set specific loggers to specific levels` {
      Logger.getLogger("com.codahale").getLevel must be(Level.ALL)
    }
  }
}
