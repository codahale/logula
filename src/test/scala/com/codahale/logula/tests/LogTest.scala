package com.codahale.logula.tests

import org.scalatest.matchers.MustMatchers
import com.codahale.logula.Log
import org.scalatest.{OneInstancePerTest, Spec}
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito.{verify, when, never}
import org.mockito.Matchers.{any}
import org.mockito.ArgumentCaptor
import java.util.logging.{Level, LogRecord, Logger}

class LogExample(log: Log) {
  def doFinest() = log.finest("One, two, %d", 3)
  def doFinest(t: Throwable) = log.finest(t, "One, two, %d", 3)

  def doFiner() = log.finer("One, two, %d", 3)
  def doFiner(t: Throwable) = log.finer(t, "One, two, %d", 3)

  def doFine() = log.fine("One, two, %d", 3)
  def doFine(t: Throwable) = log.fine(t, "One, two, %d", 3)

  def doConfig() = log.config("One, two, %d", 3)
  def doConfig(t: Throwable) = log.config(t, "One, two, %d", 3)

  def doInfo() = log.info("One, two, %d", 3)
  def doInfo(t: Throwable) = log.info(t, "One, two, %d", 3)

  def doWarning() = log.warning("One, two, %d", 3)
  def doWarning(t: Throwable) = log.warning(t, "One, two, %d", 3)

  def doSevere() = log.severe("One, two, %d", 3)
  def doSevere(t: Throwable) = log.severe(t, "One, two, %d", 3)

}

class LogTest extends Spec with MustMatchers with OneInstancePerTest with MockitoSugar {
  describe("logging a FINEST message") {
    val (logger, example) = setup(Level.FINEST)

    it("sends a log record to the logger") {
      example.doFinest()
      checkRecordSent(logger, "com.codahale.logula.tests.LogExample", "doFinest", Level.FINEST, "One, two, 3")
    }
  }

  describe("logging a FINEST message with an exception") {
    val t = mock[Throwable]
    val (logger, example) = setup(Level.FINEST)

    it("sends a log record to the logger") {
      example.doFinest(t)
      checkRecordSent(logger, "com.codahale.logula.tests.LogExample", "doFinest", Level.FINEST, "One, two, 3", t)
    }
  }

  describe("logging a FINEST message when FINEST is disabled") {
    val (logger, example) = setup()

    it("does not send log record to the logger") {
      example.doFinest()
      example.doFinest(null)
      checkRecordNotSent(logger)
    }
  }
  
  describe("logging a FINER message") {
    val (logger, example) = setup(Level.FINER)

    it("sends a log record to the logger") {
      example.doFiner()
      checkRecordSent(logger, "com.codahale.logula.tests.LogExample", "doFiner", Level.FINER, "One, two, 3")
    }
  }

  describe("logging a FINER message with an exception") {
    val t = mock[Throwable]
    val (logger, example) = setup(Level.FINER)

    it("sends a log record to the logger") {
      example.doFiner(t)
      checkRecordSent(logger, "com.codahale.logula.tests.LogExample", "doFiner", Level.FINER, "One, two, 3", t)
    }
  }

  describe("logging a FINER message when FINER is disabled") {
    val (logger, example) = setup()

    it("does not send log record to the logger") {
      example.doFiner()
      example.doFiner(null)
      checkRecordNotSent(logger)
    }
  }
  
  describe("logging a FINE message") {
    val (logger, example) = setup(Level.FINE)

    it("sends a log record to the logger") {
      example.doFine()
      checkRecordSent(logger, "com.codahale.logula.tests.LogExample", "doFine", Level.FINE, "One, two, 3")
    }
  }

  describe("logging a FINE message with an exception") {
    val t = mock[Throwable]
    val (logger, example) = setup(Level.FINE)

    it("sends a log record to the logger") {
      example.doFine(t)
      checkRecordSent(logger, "com.codahale.logula.tests.LogExample", "doFine", Level.FINE, "One, two, 3", t)
    }
  }

  describe("logging a FINE message when FINE is disabled") {
    val (logger, example) = setup()

    it("does not send log record to the logger") {
      example.doFine()
      example.doFine(null)
      checkRecordNotSent(logger)
    }
  }
  
  describe("logging a CONFIG message") {
    val (logger, example) = setup(Level.CONFIG)

    it("sends a log record to the logger") {
      example.doConfig()
      checkRecordSent(logger, "com.codahale.logula.tests.LogExample", "doConfig", Level.CONFIG, "One, two, 3")
    }
  }

  describe("logging a CONFIG message with an exception") {
    val t = mock[Throwable]
    val (logger, example) = setup(Level.CONFIG)

    it("sends a log record to the logger") {
      example.doConfig(t)
      checkRecordSent(logger, "com.codahale.logula.tests.LogExample", "doConfig", Level.CONFIG, "One, two, 3", t)
    }
  }

  describe("logging a CONFIG message when CONFIG is disabled") {
    val (logger, example) = setup()

    it("does not send log record to the logger") {
      example.doConfig()
      example.doConfig(null)
      checkRecordNotSent(logger)
    }
  }
  
  describe("logging a INFO message") {
    val (logger, example) = setup(Level.INFO)

    it("sends a log record to the logger") {
      example.doInfo()
      checkRecordSent(logger, "com.codahale.logula.tests.LogExample", "doInfo", Level.INFO, "One, two, 3")
    }
  }

  describe("logging a INFO message with an exception") {
    val t = mock[Throwable]
    val (logger, example) = setup(Level.INFO)

    it("sends a log record to the logger") {
      example.doInfo(t)
      checkRecordSent(logger, "com.codahale.logula.tests.LogExample", "doInfo", Level.INFO, "One, two, 3", t)
    }
  }

  describe("logging a INFO message when INFO is disabled") {
    val (logger, example) = setup()

    it("does not send log record to the logger") {
      example.doInfo()
      example.doInfo(null)
      checkRecordNotSent(logger)
    }
  }
  
  describe("logging a WARNING message") {
    val (logger, example) = setup(Level.WARNING)

    it("sends a log record to the logger") {
      example.doWarning()
      checkRecordSent(logger, "com.codahale.logula.tests.LogExample", "doWarning", Level.WARNING, "One, two, 3")
    }
  }

  describe("logging a WARNING message with an exception") {
    val t = mock[Throwable]
    val (logger, example) = setup(Level.WARNING)

    it("sends a log record to the logger") {
      example.doWarning(t)
      checkRecordSent(logger, "com.codahale.logula.tests.LogExample", "doWarning", Level.WARNING, "One, two, 3", t)
    }
  }

  describe("logging a WARNING message when WARNING is disabled") {
    val (logger, example) = setup()

    it("does not send log record to the logger") {
      example.doWarning()
      example.doWarning(null)
      checkRecordNotSent(logger)
    }
  }
  
  describe("logging a SEVERE message") {
    val (logger, example) = setup(Level.SEVERE)

    it("sends a log record to the logger") {
      example.doSevere()
      checkRecordSent(logger, "com.codahale.logula.tests.LogExample", "doSevere", Level.SEVERE, "One, two, 3")
    }
  }

  describe("logging a SEVERE message with an exception") {
    val t = mock[Throwable]
    val (logger, example) = setup(Level.SEVERE)

    it("sends a log record to the logger") {
      example.doSevere(t)
      checkRecordSent(logger, "com.codahale.logula.tests.LogExample", "doSevere", Level.SEVERE, "One, two, 3", t)
    }
  }

  describe("logging a SEVERE message when SEVERE is disabled") {
    val (logger, example) = setup()

    it("does not send log record to the logger") {
      example.doSevere()
      example.doSevere(null)
      checkRecordNotSent(logger)
    }
  }

  private def checkRecordSent(logger: Logger, className: String, methodName: String, level: Level, message: String, t: Throwable = null) {
    val captor = ArgumentCaptor.forClass(classOf[LogRecord])
    verify(logger).log(captor.capture)

    val record = captor.getValue
    record.getSourceClassName must equal(className)
    record.getSourceMethodName must equal(methodName)
    record.getLevel must equal(level)
    record.getMessage must equal(message)
    record.getThrown must equal(t)
  }

  private def checkRecordNotSent(logger: Logger) {
    verify(logger, never).log(any(classOf[LogRecord]))
  }

  private def setup(levels: Level*) = {
    val logger = mock[Logger]
    when(logger.isLoggable(any(classOf[Level]))).thenReturn(false)
    for (level <- levels) {
      when(logger.isLoggable(level)).thenReturn(true)
    }
    val example = new LogExample(new Log(classOf[LogExample], logger))
    (logger, example)
  }
}
