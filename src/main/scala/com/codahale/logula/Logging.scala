package com.codahale.logula

import org.apache.log4j.rolling.{TimeBasedRollingPolicy, RollingFileAppender}
import management.ManagementFactory
import javax.management.{InstanceAlreadyExistsException, ObjectName}
import org.apache.log4j.{Level, Logger, ConsoleAppender, AsyncAppender}
import java.io.PrintStream

/**
 * A singleton class for configuring logging in a JVM process.
 */
object Logging {
  /**
   * Disables all logging output. (Useful for unit tests.)
   */
  def silence() {
    reset()
    logToConsole(Level.OFF)
  }

  /**
   * Logs all output at or above the specified level to STDOUT and sets the
   * provided specific logger levels, if any.
   */
  def logToConsole(default: Level, levels: (String, Level)*) {
    val root = Logger.getRootLogger()
    root.setLevel(default)

    val formatter = new Formatter

    val appender = new AsyncAppender
    appender.addAppender(new ConsoleAppender(formatter))
    root.addAppender(appender)

    setLevels(levels)
  }

  /**
   * Logs all output at or above the specified level to the specified file and
   * sets the provided specific logger levels, if any.
   *
   * See {@link org.apache.log4j.rolling.TimeBasedRollingPolicy} for details.
   */
  def logToFile(default: Level,
                filenamePattern: String,
                levels: (String, Level)*) {
    val root = Logger.getRootLogger()
    root.setLevel(default)
    val formatter = new Formatter

    val policy = new TimeBasedRollingPolicy
    policy.setFileNamePattern(filenamePattern)
    policy.activateOptions()

    val rollingLog = new RollingFileAppender()
    rollingLog.setLayout(formatter)
    rollingLog.setRollingPolicy(policy)
    rollingLog.activateOptions()
    rollingLog.setThreshold(Level.ALL)

    val appender = new AsyncAppender
    appender.addAppender(rollingLog)
    root.addAppender(appender)

    setLevels(levels)
  }

  /**
   * Registers a JMX MBean which allows you to set logger levels at runtime via
   * JXM.
   */
  def registerWithJMX() {
    try {
      val mbeans = ManagementFactory.getPlatformMBeanServer
      val name = new ObjectName("com.codahale.logula:type=Logging")
      mbeans.registerMBean(LoggingMXBean, name)
    } catch {
      case e: InstanceAlreadyExistsException => // THANKS
    }
  }

  /**
   * Resets all existing logging configuration.
   */
  def reset() {
    val root = Logger.getRootLogger()
    root.getLoggerRepository.resetConfiguration()
  }

  private var originalStdOut: Option[PrintStream] = None
  private var originalStdErr: Option[PrintStream] = None

  def redirectStdOut(level: Level) = synchronized {
    if (originalStdOut.isEmpty) {
      originalStdOut = Some(System.out)
      System.setOut(
        new PrintStream(
          new LoggerOutputStream(Logger.getLogger("system.stdout"), level),
          true
        )
      )
    }
  }

  def restoreStdOut() = synchronized {
    originalStdOut.foreach { out =>
      System.setOut(out)
      originalStdOut = None
    }
  }

  def redirectStdErr(level: Level) = synchronized {
    if (originalStdErr.isEmpty) {
      originalStdErr = Some(System.err)
      System.setErr(
        new PrintStream(
          new LoggerOutputStream(Logger.getLogger("system.stderr"), level),
          true
        )
      )
    }
  }

  def restoreStdErr() = synchronized {
    originalStdErr.foreach{ err =>
        System.setErr(err)
        originalStdErr = None
    }
  }

  private def setLevels(levels: Seq[(String, Level)]) {
    for ((name, level) <- levels) {
      Logger.getLogger(name).setLevel(level)
    }
  }
}

/**
 * A mixin trait which provides subclasses with a Log instance keyed to the
 * subclass's name.
 */
trait Logging {
  protected lazy val log: Log = Log.forClass(getClass)
}
