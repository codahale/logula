package com.codahale.logula

import org.apache.log4j.{Level, Logger}
import reflect.NameTransformer

/**
 * Log's companion object.
 */
object Log {
  /**
   * Returns a log for a given class.
   */
  def forClass[A](implicit mf: Manifest[A]) = forName(mf.erasure.getName)

  /**
   * Returns a log for a given class.
   */
  def forClass(klass: Class[_]) = forName(klass.getName)

  /**
   * Returns a log with the given name.
   */
  def forName(name: String) = new Log(Logger.getLogger(clean(name)))

  private def clean(s: String) = NameTransformer.decode(if (s.endsWith("$")) {
    s.substring(0, s.length - 1)
  } else {
    s
  }.replaceAll("\\$", "."))

  protected val CallerFQCN = classOf[Log].getCanonicalName
}

/**
 * <b>In general, use the Logging trait rather than using Log directly.</b>
 *
 * A wrapper for org.apache.log4j which allows for a smoother interaction with
 * Scala classes. All messages are treated as format strings:
 * <pre>
 *    log.info("We have this many threads: %d", thread.size)
 * </pre>
 * Each log level has two methods: one for logging regular messages, the other
 * for logging messages with thrown exceptions.
 *
 * The log levels here are those of org.apache.log4j.
 *
 * @author coda
 */
class Log(private val logger: Logger) {
  import Log._
  
  /**
   * Logs a message with optional parameters at level TRACE.
   */
  def trace(message: String, params: Any*) {
    log(Level.TRACE, None, message, params)
  }

  /**
   * Logs a thrown exception and a message with optional parameters at level
   * TRACE.
   */
  def trace(thrown: Throwable, message: String, params: Any*) {
    log(Level.TRACE, Some(thrown), message, params)
  }

  /**
   * Logs a message with optional parameters at level DEBUG.
   */
  def debug(message: String, params: Any*) {
    log(Level.DEBUG, None, message, params)
  }

  /**
   * Logs a thrown exception and a message with optional parameters at level
   * DEBUG.
   */
  def debug(thrown: Throwable, message: String, params: Any*) {
    log(Level.DEBUG, Some(thrown), message, params)
  }

  /**
   * Logs a message with optional parameters at level INFO.
   */
  def info(message: String, params: Any*) {
    log(Level.INFO, None, message, params)
  }

  /**
   * Logs a thrown exception and a message with optional parameters at level
   * INFO.
   */
  def info(thrown: Throwable, message: String, params: Any*) {
    log(Level.INFO, Some(thrown), message, params)
  }

  /**
   * Logs a message with optional parameters at level WARN.
   */
  def warn(message: String, params: Any*) {
    log(Level.WARN, None, message, params)
  }

  /**
   * Logs a thrown exception and a message with optional parameters at level
   * WARN.
   */
  def warn(thrown: Throwable, message: String, params: Any*) {
    log(Level.WARN, Some(thrown), message, params)
  }

  /**
   * Logs a message with optional parameters at level ERROR.
   */
  def error(message: String, params: Any*) {
    log(Level.ERROR, None, message, params)
  }

  /**
   * Logs a thrown exception and a message with optional parameters at level
   * ERROR.
   */
  def error(thrown: Throwable, message: String, params: Any*) {
    log(Level.ERROR, Some(thrown), message, params)
  }

  /**
   * Logs a message with optional parameters at level FATAL.
   */

  def fatal(message: String, params: Any*) {
    log(Level.FATAL, None, message, params)
  }

  /**
   * Logs a thrown exception and a message with optional parameters at level
   * FATAL.
   */

  def fatal(thrown: Throwable, message: String, params: Any*) {
    log(Level.FATAL, Some(thrown), message, params)
  }

  /**
   * Returns the log's current level.
   */
  def level = logger.getLevel

  /**
   * Sets the log's current level.
   */
  def level_=(level: Level) {
    logger.setLevel(level)
  }

  def isTraceEnabled = logger.isEnabledFor(Level.TRACE)

  def isDebugEnabled = logger.isEnabledFor(Level.DEBUG)

  def isInfoEnabled = logger.isEnabledFor(Level.INFO)

  def isWarnEnabled = logger.isEnabledFor(Level.WARN)

  def isErrorEnabled = logger.isEnabledFor(Level.ERROR)

  def isFatalEnabled = logger.isEnabledFor(Level.FATAL)

  private def log(level: Level, thrown: Option[Throwable], message: String, values: Seq[Any]) {
    if (logger.isEnabledFor(level)) {
      val statement = if (values.isEmpty) message else message.format(values:_*)
      logger.log(CallerFQCN, level, statement, thrown.orNull)
    }
  }
}
