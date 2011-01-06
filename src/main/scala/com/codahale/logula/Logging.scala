package com.codahale.logula

import scala.collection.mutable
import org.apache.log4j.rolling.{TimeBasedRollingPolicy, RollingFileAppender}
import management.ManagementFactory
import javax.management.{InstanceAlreadyExistsException, ObjectName}
import org.apache.log4j.{Level, Logger, ConsoleAppender, AsyncAppender}

/**
 * A singleton class for configuring logging in a JVM process.
 */
object Logging {
  class LoggingConsoleConfig {
    /**
     * Whether or not logged statements should be output to standard out.
     * Defaults to {@code true}.
     */
    var enabled = true

    /**
     * The minimum logging level which will be written to console.
     */
    var threshold = Level.ALL
  }

  class LoggingFileConfig {
    /**
     * Whether or not logged statements should be output to a file.
     * Defaults to  {@code false}.
     */
    var enabled = false

    /**
     * The minimum logging level which will be written to the file.
     */
    var threshold = Level.ALL

    /**
     * The log filename pattern.
     *
     * See  { @link org.apache.log4j.rolling.TimeBasedRollingPolicy } for details.
     */
    var filenamePattern = ""
  }

  class LoggingConfig {
    /**
     * Console logging configuration.
     */
    val console = new LoggingConsoleConfig

    /**
     * File logging configuration.
     */
    val file = new LoggingFileConfig

    /**
     * A map of logger names to default levels.
     */
    val loggers = new mutable.HashMap[String, Level]()

    /**
     * Whether or not Logula should register a JMX MBean which allows you to
     * dynamically modify logger levels. Defaults to {@code true}.
     */
    var registerWithJMX = true

    /**
     * The default level for all loggers.
     */
    var level = Level.INFO
  }

  /**
   * Configure Logula.
   */
  def configure(f: LoggingConfig => Unit) {
    val config = new LoggingConfig
    f(config)

    val root = Logger.getRootLogger
    root.getLoggerRepository.resetConfiguration()
    root.setLevel(config.level)

    for ((name, level) <- config.loggers) {
      Logger.getLogger(name).setLevel(level)
    }

    if (config.console.enabled) {
      val appender = new AsyncAppender
      val console = new ConsoleAppender(new Formatter)
      console.setThreshold(config.console.threshold)
      appender.addAppender(console)
      root.addAppender(appender)
    }

    if (config.file.enabled) {
      val formatter = new Formatter

      val policy = new TimeBasedRollingPolicy
      policy.setFileNamePattern(config.file.filenamePattern)
      policy.activateOptions()

      val rollingLog = new RollingFileAppender()
      rollingLog.setLayout(formatter)
      rollingLog.setRollingPolicy(policy)
      rollingLog.activateOptions()
      rollingLog.setThreshold(config.file.threshold)

      val appender = new AsyncAppender
      appender.addAppender(rollingLog)
      root.addAppender(appender)
    }

    if (config.registerWithJMX) {
      try {
        val mbeans = ManagementFactory.getPlatformMBeanServer
        val name = new ObjectName("com.codahale.logula:type=Logging")
        mbeans.registerMBean(LoggingMXBean, name)
      } catch {
        case e: InstanceAlreadyExistsException => // THANKS
      }
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
