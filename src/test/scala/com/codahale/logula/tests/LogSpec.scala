package com.codahale.logula.tests

import com.codahale.simplespec.Spec
import com.codahale.logula.Log
import org.specs.mock.Mockito
import org.apache.log4j.{Logger, Level}

class LogExample(log: Log) {
  def doTrace() = log.trace("One, two, %d", 3)
  def doTrace(t: Throwable) = log.trace(t, "One, two, %d", 3)

  def doDebug() = log.debug("One, two, %d", 3)
  def doDebug(t: Throwable) = log.debug(t, "One, two, %d", 3)

  def doInfo() = log.info("One, two, %d", 3)
  def doInfo(t: Throwable) = log.info(t, "One, two, %d", 3)

  def doWarn() = log.warn("One, two, %d", 3)
  def doWarn(t: Throwable) = log.warn(t, "One, two, %d", 3)

  def doError() = log.error("One, two, %d", 3)
  def doError(t: Throwable) = log.error(t, "One, two, %d", 3)

  def doFatal() = log.fatal("One, two, %d", 3)
  def doFatal(t: Throwable) = log.fatal(t, "One, two, %d", 3)
}

object LogSpec extends Spec with Mockito {
  class `Logging a TRACE message` {
    val (logger, example) = setup(Level.TRACE)

    def `should pass the message to the underlying logger` {
      example.doTrace()
      thereWasOneLog(logger, Level.TRACE, "One, two, 3")
    }
  }

  class `Logging a TRACE message with an exception` {
    val t = mock[Throwable]
    val (logger, example) = setup(Level.TRACE)

    def `should pass the message to the underlying logger` {
      example.doTrace(t)
      thereWasOneLog(logger, Level.TRACE, "One, two, 3", t)
    }
  }

  class `Logging a TRACE message when TRACE is disabled` {
    val t = mock[Throwable]
    val (logger, example) = setup()

    def `should not pass the message to the underlying logger` {
      example.doTrace()
      example.doTrace(t)
      thereWereNoLogs(logger)
    }
  }

  class `Logging a DEBUG message` {
    val (logger, example) = setup(Level.DEBUG)

    def `should pass the message to the underlying logger` {
      example.doDebug()
      thereWasOneLog(logger, Level.DEBUG, "One, two, 3")
    }
  }

  class `Logging a DEBUG message with an exception` {
    val t = mock[Throwable]
    val (logger, example) = setup(Level.DEBUG)

    def `should pass the message to the underlying logger` {
      example.doDebug(t)
      thereWasOneLog(logger, Level.DEBUG, "One, two, 3", t)
    }
  }

  class `Logging a DEBUG message when DEBUG is disabled` {
    val t = mock[Throwable]
    val (logger, example) = setup()

    def `should not pass the message to the underlying logger` {
      example.doDebug()
      example.doDebug(t)
      thereWereNoLogs(logger)
    }
  }

  class `Logging a INFO message` {
    val (logger, example) = setup(Level.INFO)

    def `should pass the message to the underlying logger` {
      example.doInfo()
      thereWasOneLog(logger, Level.INFO, "One, two, 3")
    }
  }

  class `Logging a INFO message with an exception` {
    val t = mock[Throwable]
    val (logger, example) = setup(Level.INFO)

    def `should pass the message to the underlying logger` {
      example.doInfo(t)
      thereWasOneLog(logger, Level.INFO, "One, two, 3", t)
    }
  }

  class `Logging a INFO message when INFO is disabled` {
    val t = mock[Throwable]
    val (logger, example) = setup()

    def `should not pass the message to the underlying logger` {
      example.doInfo()
      example.doInfo(t)
      thereWereNoLogs(logger)
    }
  }

  class `Logging a WARN message` {
    val (logger, example) = setup(Level.WARN)

    def `should pass the message to the underlying logger` {
      example.doWarn()
      thereWasOneLog(logger, Level.WARN, "One, two, 3")
    }
  }

  class `Logging a WARN message with an exception` {
    val t = mock[Throwable]
    val (logger, example) = setup(Level.WARN)

    def `should pass the message to the underlying logger` {
      example.doWarn(t)
      thereWasOneLog(logger, Level.WARN, "One, two, 3", t)
    }
  }

  class `Logging a WARN message when WARN is disabled` {
    val t = mock[Throwable]
    val (logger, example) = setup()

    def `should not pass the message to the underlying logger` {
      example.doWarn()
      example.doWarn(t)
      thereWereNoLogs(logger)
    }
  }

  class `Logging a ERROR message` {
    val (logger, example) = setup(Level.ERROR)

    def `should pass the message to the underlying logger` {
      example.doError()
      thereWasOneLog(logger, Level.ERROR, "One, two, 3")
    }
  }

  class `Logging a ERROR message with an exception` {
    val t = mock[Throwable]
    val (logger, example) = setup(Level.ERROR)

    def `should pass the message to the underlying logger` {
      example.doError(t)
      thereWasOneLog(logger, Level.ERROR, "One, two, 3", t)
    }
  }

  class `Logging a ERROR message when ERROR is disabled` {
    val t = mock[Throwable]
    val (logger, example) = setup()

    def `should not pass the message to the underlying logger` {
      example.doError()
      example.doError(t)
      thereWereNoLogs(logger)
    }
  }

  class `Logging a FATAL message` {
    val (logger, example) = setup(Level.FATAL)

    def `should pass the message to the underlying logger` {
      example.doFatal()
      thereWasOneLog(logger, Level.FATAL, "One, two, 3")
    }
  }

  class `Logging a FATAL message with an exception` {
    val t = mock[Throwable]
    val (logger, example) = setup(Level.FATAL)

    def `should pass the message to the underlying logger` {
      example.doFatal(t)
      thereWasOneLog(logger, Level.FATAL, "One, two, 3", t)
    }
  }

  class `Logging a FATAL message when FATAL is disabled` {
    val t = mock[Throwable]
    val (logger, example) = setup()

    def `should not pass the message to the underlying logger` {
      example.doFatal()
      example.doFatal(t)
      thereWereNoLogs(logger)
    }
  }

  private def thereWasOneLog(logger: Logger, level: Level, message: String, t: Throwable = null) {
    there was one(logger).log("com.codahale.logula.Log", level, message, t)
  }

  private def thereWereNoLogs(logger: Logger) {
    there were no(logger).log(any[String], any[Level], any[Object], any[Throwable])
  }

  private def setup(levels: Level*) = {
    val logger = mock[Logger]

    logger.isEnabledFor(any[Level]) returns false

    for (level <- levels) {
      logger.isEnabledFor(level) returns true
    }
    
    val example = new LogExample(new Log(classOf[LogExample], logger))
    (logger, example)
  }
}
