package com.codahale.logula

import org.apache.log4j.{Logger, Level}

/**
 * A JMX MBean which allows logger levels to be set at runtime.
 *
 * @author coda
 */
trait LoggingMXBean {
  /**
   * Returns the logger's effective level as a string.
   */
  def getLoggerLevel(name: String): String

  /**
   * Sets the specified logger's level to the specific level.
   *
   * (If the level name is unknown, INFO is used as a default.)
   */
  def setLoggerLevel(name: String, level: String): String
}

/**
 * The LoggingMXBean implementation.
 */
object LoggingMXBean extends LoggingMXBean {
  def setLoggerLevel(name: String, levelName: String) = {
    val level = Level.toLevel(levelName, Level.INFO)
    val logger = Logger.getLogger(name)
    logger.setLevel(level)
    "set to " + logger.getLevel
  }

  def getLoggerLevel(name: String) = {
    val logger = Logger.getLogger(name)
    logger.getEffectiveLevel.toString
  }
}
