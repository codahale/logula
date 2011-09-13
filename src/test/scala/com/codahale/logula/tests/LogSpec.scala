package com.codahale.logula.tests

import com.codahale.logula.Log
import org.apache.log4j.{Logger, Level}
import com.codahale.simplespec.Spec
import org.junit.Test

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

class LogSpec extends Spec {
  val logger = mock[Logger]
  val example = new LogExample(new Log(logger))

  class `Logging a TRACE message` {
    logger.isEnabledFor(Level.TRACE).returns(true)

    @Test def `passes the message to the underlying logger` = {
      example.doTrace()
      verify.one(logger).log("com.codahale.logula.Log", Level.TRACE, "One, two, 3", null)
    }

    class `with an exception` {
      val t = mock[Throwable]

      @Test def `passes the message to the underlying logger` = {
        example.doTrace(t)
        verify.one(logger).log("com.codahale.logula.Log", Level.TRACE, "One, two, 3", t)
      }
    }

    class `when TRACE is disabled` {
      val t = mock[Throwable]
      logger.isEnabledFor(Level.TRACE).returns(false)

      @Test def `doesn't pass the message to the underlying logger` = {
        example.doTrace()
        example.doTrace(t)
        verify.no(logger).log(any, any, any)
      }
    }
  }

  class `Logging a DEBUG message` {
    logger.isEnabledFor(Level.DEBUG).returns(true)

    @Test def `passes the message to the underlying logger` = {
      example.doDebug()
      verify.one(logger).log("com.codahale.logula.Log", Level.DEBUG, "One, two, 3", null)
    }

    class `with an exception` {
      val t = mock[Throwable]

      @Test def `passes the message to the underlying logger` = {
        example.doDebug(t)
        verify.one(logger).log("com.codahale.logula.Log", Level.DEBUG, "One, two, 3", t)
      }
    }

    class `when DEBUG is disabled` {
      val t = mock[Throwable]
      logger.isEnabledFor(Level.DEBUG).returns(false)

      @Test def `doesn't pass the message to the underlying logger` = {
        example.doDebug()
        example.doDebug(t)
        verify.no(logger).log(any, any, any)
      }
    }
  }

  class `Logging a INFO message` {
    logger.isEnabledFor(Level.INFO).returns(true)

    @Test def `passes the message to the underlying logger` = {
      example.doInfo()
      verify.one(logger).log("com.codahale.logula.Log", Level.INFO, "One, two, 3", null)
    }

    class `with an exception` {
      val t = mock[Throwable]

      @Test def `passes the message to the underlying logger` = {
        example.doInfo(t)
        verify.one(logger).log("com.codahale.logula.Log", Level.INFO, "One, two, 3", t)
      }
    }

    class `when INFO is disabled` {
      val t = mock[Throwable]
      logger.isEnabledFor(Level.INFO).returns(false)

      @Test def `doesn't pass the message to the underlying logger` = {
        example.doInfo()
        example.doInfo(t)
        verify.no(logger).log(any, any, any)
      }
    }
  }

  class `Logging a WARN message` {
    logger.isEnabledFor(Level.WARN).returns(true)

    @Test def `passes the message to the underlying logger` = {
      example.doWarn()
      verify.one(logger).log("com.codahale.logula.Log", Level.WARN, "One, two, 3", null)
    }

    class `with an exception` {
      val t = mock[Throwable]

      @Test def `passes the message to the underlying logger` = {
        example.doWarn(t)
        verify.one(logger).log("com.codahale.logula.Log", Level.WARN, "One, two, 3", t)
      }
    }

    class `when WARN is disabled` {
      val t = mock[Throwable]
      logger.isEnabledFor(Level.WARN).returns(false)

      @Test def `doesn't pass the message to the underlying logger` = {
        example.doWarn()
        example.doWarn(t)
        verify.no(logger).log(any, any, any)
      }
    }
  }

  class `Logging a ERROR message` {
    logger.isEnabledFor(Level.ERROR).returns(true)

    @Test def `passes the message to the underlying logger` = {
      example.doError()
      verify.one(logger).log("com.codahale.logula.Log", Level.ERROR, "One, two, 3", null)
    }

    class `with an exception` {
      val t = mock[Throwable]

      @Test def `passes the message to the underlying logger` = {
        example.doError(t)
        verify.one(logger).log("com.codahale.logula.Log", Level.ERROR, "One, two, 3", t)
      }
    }

    class `when ERROR is disabled` {
      val t = mock[Throwable]
      logger.isEnabledFor(Level.ERROR).returns(false)

      @Test def `doesn't pass the message to the underlying logger` = {
        example.doError()
        example.doError(t)
        verify.no(logger).log(any, any, any)
      }
    }
  }

  class `Logging a FATAL message` {
    logger.isEnabledFor(Level.FATAL).returns(true)

    @Test def `passes the message to the underlying logger` = {
      example.doFatal()
      verify.one(logger).log("com.codahale.logula.Log", Level.FATAL, "One, two, 3", null)
    }

    class `with an exception` {
      val t = mock[Throwable]

      @Test def `passes the message to the underlying logger` = {
        example.doFatal(t)
        verify.one(logger).log("com.codahale.logula.Log", Level.FATAL, "One, two, 3", t)
      }
    }

    class `when FATAL is disabled` {
      val t = mock[Throwable]
      logger.isEnabledFor(Level.FATAL).returns(false)

      @Test def `doesn't pass the message to the underlying logger` = {
        example.doFatal()
        example.doFatal(t)
        verify.no(logger).log(any, any, any)
      }
    }
  }
}
