package com.codahale.logula

import scala.collection.mutable
import org.apache.log4j.rolling.{TimeBasedRollingPolicy, RollingFileAppender}
import management.ManagementFactory
import javax.management.{InstanceAlreadyExistsException, ObjectName}
import org.apache.log4j.{Level, Logger, ConsoleAppender, AsyncAppender}
import java.util.concurrent.{ThreadFactory, TimeUnit, Executors}

/**
 * A singleton class for configuring logging in a JVM process.
 */
object Logging {
  private val e = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
    def newThread(r: Runnable) = {
      val t = new Thread() {
        override def run() = r.run()
      }
      t.setDaemon(true)
      t.setName("Logula-GCMonitor")
      t
    }
  })

  class LoggingGCConfig {
    /**
     * Whether or not GC activity should be logged. Defaults to {@code false}.
     */
    var enabled = false
    protected[logula] var interval = TimeUnit.SECONDS.toMillis(1)
    protected[logula] val minDurations = new mutable.HashMap[Level, Long]
    minDurations(Level.TRACE) =    0
    minDurations(Level.DEBUG) =  100
    minDurations(Level.INFO)  =  500
    minDurations(Level.WARN) =  1000

    /**
     * Sets the interval at which GC activity is analyzed. Defaults to 1 sec.
     */
    def checkEvery(interval: Long, unit: TimeUnit) {
      this.interval = unit.toMillis(interval)
    }

    /**
     * Adds the threshold at which garbage collection runs should be logged at
     * a specific level.
     *
     * For example, {@code addDurationThreshold (Level.WARN, 1, TimeUnit.SECONDS)}
     * would log all GC runs which took 1 second or longer at {@code WARN}
     * level.
     */
    def addDurationThreshold(level: Level, duration: Long, unit: TimeUnit) {
      minDurations(level) = unit.toMillis(duration)
    }
  }

  class LoggingConsoleConfig {
    /**
     * Whether or not logged statements should be output to standard out.
     * Defaults to {@code true}.
     */
    var enabled = true

    /**
     * The minimum logging level which will be written to console.
     */
    var level = Level.INFO
  }

  class LoggingFileConfig {
    /**
     * Whether or not logged statements should be output to a file.
     * Defaults to  {@code false}.
     */
    var enabled = true

    /**
     * The minimum logging level which will be written to the file.
     */
    var level = Level.INFO

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
     * GC logging configuration.
     */
    val gc = new LoggingGCConfig

    /**
     * A map of logger names to default levels.
     */
    val loggers = new mutable.HashMap[String, Level]()

    /**
     * Whether or not Logula should register a JMX MBean which allows you to
     * dynamically modify logger levels. Defaults to {@code true}.
     */
    var registerWithJMX = true
  }

  /**
   * Configure Logula.
   */
  def configure(f: LoggingConfig => Unit) {
    val config = new LoggingConfig
    f(config)

    val root = Logger.getRootLogger
    root.getLoggerRepository.resetConfiguration()

    for ((name, level) <- config.loggers) {
      Logger.getLogger(name).setLevel(level)
    }

    if (config.console.enabled) {
      val appender = new AsyncAppender
      val console = new ConsoleAppender(new Formatter)
      console.setThreshold(config.console.level)
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
      rollingLog.setThreshold(config.file.level)

      val appender = new AsyncAppender
      appender.addAppender(rollingLog)
      root.addAppender(appender)
    }

    if (config.gc.enabled) {
      val logger = Logger.getLogger("GC")
      e.scheduleAtFixedRate(
        new GCMonitor(logger, config.gc.minDurations.toSeq),
        0, config.gc.interval._1, config.gc.interval._2
      )
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
