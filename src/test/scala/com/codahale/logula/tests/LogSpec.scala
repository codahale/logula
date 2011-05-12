package com.codahale.logula.tests

import com.codahale.logula.Log
import org.apache.log4j.{Logger, Level}
import org.specs2.mock.Mockito
import com.codahale.simplespec.{ignore, Spec}

class LogExample(log: Log) {
  def doTrace() { log.trace("One, two, %d", 3) }
  def doTrace(t: Throwable) { log.trace(t, "One, two, %d", 3) }
  def doDebug() { log.debug("One, two, %d", 3) }
  def doDebug(t: Throwable) { log.debug(t, "One, two, %d", 3) }
  def doInfo() { log.info("One, two, %d", 3) }
  def doInfo(t: Throwable) { log.info(t, "One, two, %d", 3) }
  def doWarn() { log.warn("One, two, %d", 3) }
  def doWarn(t: Throwable) { log.warn(t, "One, two, %d", 3) }
  def doError() { log.error("One, two, %d", 3) }
  def doError(t: Throwable) { log.error(t, "One, two, %d", 3) }
  def doFatal() { log.fatal("One, two, %d", 3) }
  def doFatal(t: Throwable) { log.fatal(t, "One, two, %d", 3) }
}

object LogSpec extends Spec with Mockito {
  @ignore
  abstract class Context(levels: Level*) {
    protected val logger = mock[Logger]
    logger.isEnabledFor(any[Level]) returns false
    for (level <- levels) {
      logger.isEnabledFor(level) returns true
    }
    protected val example = new LogExample(new Log(logger))

    protected def thereWasOneLog(logger: Logger, level: Level, message: String, t: Throwable = null) = {
      there was one(logger).log("com.codahale.logula.Log", level, message, t)
    }

    protected def thereWereNoLogs(logger: Logger) = {
      there were no(logger).log(any[String], any[Level], any[Object], any[Throwable])
    }
  }

  class `Logging a TRACE message` extends Context(Level.TRACE) {
    def `passes the message to the underlying logger` = {
      example.doTrace()
      thereWasOneLog(logger, Level.TRACE, "One, two, 3")
    }

    class `with an exception` {
      private val t = mock[Throwable]

      def `passes the message to the underlying logger` = {
        example.doTrace(t)
        thereWasOneLog(logger, Level.TRACE, "One, two, 3", t)
      }
    }

    class `when TRACE is disabled` {
      private val t = mock[Throwable]
      logger.isEnabledFor(Level.TRACE) returns false

      def `doesn't pass the message to the underlying logger` = {
        example.doTrace()
        example.doTrace(t)
        thereWereNoLogs(logger)
      }
    }
  }

  class `Logging a DEBUG message` extends Context(Level.DEBUG) {
    def `passes the message to the underlying logger` = {
      example.doDebug()
      thereWasOneLog(logger, Level.DEBUG, "One, two, 3")
    }

    class `with an exception` {
      private val t = mock[Throwable]

      def `passes the message to the underlying logger` = {
        example.doDebug(t)
        thereWasOneLog(logger, Level.DEBUG, "One, two, 3", t)
      }
    }

    class `when DEBUG is disabled` {
      private val t = mock[Throwable]
      logger.isEnabledFor(Level.DEBUG) returns false

      def `doesn't pass the message to the underlying logger` = {
        example.doDebug()
        example.doDebug(t)
        thereWereNoLogs(logger)
      }
    }
  }

  class `Logging a INFO message` extends Context(Level.INFO) {
    def `passes the message to the underlying logger` = {
      example.doInfo()
      thereWasOneLog(logger, Level.INFO, "One, two, 3")
    }

    class `with an exception` {
      private val t = mock[Throwable]

      def `passes the message to the underlying logger` = {
        example.doInfo(t)
        thereWasOneLog(logger, Level.INFO, "One, two, 3", t)
      }
    }

    class `when INFO is disabled` {
      private val t = mock[Throwable]
      logger.isEnabledFor(Level.INFO) returns false

      def `doesn't pass the message to the underlying logger` = {
        example.doInfo()
        example.doInfo(t)
        thereWereNoLogs(logger)
      }
    }
  }

  class `Logging a WARN message` extends Context(Level.WARN) {
    def `passes the message to the underlying logger` = {
      example.doWarn()
      thereWasOneLog(logger, Level.WARN, "One, two, 3")
    }

    class `with an exception` {
      private val t = mock[Throwable]

      def `passes the message to the underlying logger` = {
        example.doWarn(t)
        thereWasOneLog(logger, Level.WARN, "One, two, 3", t)
      }
    }

    class `when WARN is disabled` {
      private val t = mock[Throwable]
      logger.isEnabledFor(Level.WARN) returns false

      def `doesn't pass the message to the underlying logger` = {
        example.doWarn()
        example.doWarn(t)
        thereWereNoLogs(logger)
      }
    }
  }

  class `Logging a ERROR message` extends Context(Level.ERROR) {
    def `passes the message to the underlying logger` = {
      example.doError()
      thereWasOneLog(logger, Level.ERROR, "One, two, 3")
    }

    class `with an exception` {
      private val t = mock[Throwable]

      def `passes the message to the underlying logger` = {
        example.doError(t)
        thereWasOneLog(logger, Level.ERROR, "One, two, 3", t)
      }
    }

    class `when ERROR is disabled` {
      private val t = mock[Throwable]
      logger.isEnabledFor(Level.ERROR) returns false

      def `doesn't pass the message to the underlying logger` = {
        example.doError()
        example.doError(t)
        thereWereNoLogs(logger)
      }
    }
  }

  class `Logging a FATAL message` extends Context(Level.FATAL) {
    def `passes the message to the underlying logger` = {
      example.doFatal()
      thereWasOneLog(logger, Level.FATAL, "One, two, 3")
    }

    class `with an exception` {
      private val t = mock[Throwable]

      def `passes the message to the underlying logger` = {
        example.doFatal(t)
        thereWasOneLog(logger, Level.FATAL, "One, two, 3", t)
      }
    }

    class `when FATAL is disabled` {
      private val t = mock[Throwable]
      logger.isEnabledFor(Level.FATAL) returns false

      def `doesn't pass the message to the underlying logger` = {
        example.doFatal()
        example.doFatal(t)
        thereWereNoLogs(logger)
      }
    }
  }
}
