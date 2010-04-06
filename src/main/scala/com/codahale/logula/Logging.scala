package com.codahale.logula

import collection.JavaConversions.asIterator
import java.util.logging.{ConsoleHandler, Logger, LogManager, Level}

/**
 * A singleton class for configuring logging in a JVM process.
 */
object Logging {
  /**
   * Enables Logula's formatter, sets the default level, and sets any provided
   * specific logger levels.
   */
  def configure(default: Level, levels: (String, Level)*) {
    resetHandlers()
    addOwnHandler(default)
    setLevels(levels.toMap)
  }

  private def setLevels(levels: Map[String, Level]) {
    for ((name, level) <- levels) {
      Logger.getLogger(name).setLevel(level)
    }
  }

  private def addOwnHandler(default: Level) {
    val root = Logger.getLogger("")
    root.setLevel(default)
    val handler = new ConsoleHandler
    handler.setFormatter(new Formatter)
    handler.setLevel(Level.ALL)
    root.addHandler(handler)
  }

  private def resetHandlers() {
    for (name <- loggerNames) {
      val logger = Logger.getLogger(name)
      for (handler <- logger.getHandlers) {
        handler.close()
        logger.removeHandler(handler)
      }
    }
  }

  private def loggerNames =
    asIterator(LogManager.getLogManager.getLoggerNames).filter { _ != null }
}

/**
 * A mixin trait which provides subclasses with a Log instance keyed to the
 * subclass's name.
 */
trait Logging {
  protected lazy val log: Log = Log.forClass(getClass)
}
