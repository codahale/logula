package com.codahale.logula

import scala.collection.mutable
import management.ManagementFactory
import javax.management.{InstanceAlreadyExistsException, ObjectName}
import org.apache.log4j.net.SyslogAppender
import org.apache.log4j._

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
     * The log filename.
     */
    var filename = ""

    /**
     * The maximum log file in kilobytes, before it's rolled over.
     */
    var maxSize = 10 * 1024 //10MB

    /**
     * The number of old log files to retain.
     */
    var retainedFiles = 5
  }

  class LoggingSyslogConfig {
    /**
     * Whether or not logged statements should be output to syslog.
     * Defaults to  {@code false}.
     */
    var enabled = false

    /**
     * The syslog host.
     */
    var host = "localhost"

    /**
     * The syslog facility.
     */
    var facility = "local0"
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
     * Syslog logging configuration.
     */
    val syslog = new LoggingSyslogConfig

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
   * Configure Logula using all defaults
   */
  def configure() {
    configure { _ => }
  }

  /**
   * Configure Logula.
   */
  def configure(f: LoggingConfig => Any) {
    val config = new LoggingConfig
    f(config)

    val root = Logger.getRootLogger
    root.getLoggerRepository.resetConfiguration()
    root.setLevel(config.level)

    for ((name, level) <- config.loggers) {
      Logger.getLogger(name).setLevel(level)
    }

    val appender = new AsyncAppender
    root.addAppender(appender)

    if (config.console.enabled) {
      val console = new ConsoleAppender(new Formatter)
      console.setThreshold(config.console.threshold)
      appender.addAppender(console)
    }

    if (config.file.enabled) {
      val rollingLog = new RollingFileAppender()
      rollingLog.setLayout(new Formatter)
      rollingLog.setAppend(true)
      rollingLog.setFile(config.file.filename)
      rollingLog.setMaximumFileSize(config.file.maxSize * 1024)
      rollingLog.setMaxBackupIndex(config.file.retainedFiles)
      rollingLog.activateOptions()
      rollingLog.setThreshold(config.file.threshold)

      appender.addAppender(rollingLog)
    }

    if (config.syslog.enabled) {
      val layout = new PatternLayout("%c: %m")
      val syslog = new SyslogAppender(layout, "localhost", SyslogAppender.getFacility("local2"))
      appender.addAppender(syslog)
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
