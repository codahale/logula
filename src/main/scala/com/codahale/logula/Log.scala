package com.codahale.logula

import java.util.logging.{LogRecord, Level, Logger}

/**
 * Log's companion object.
 */
object Log {
  /**
   * Returns a log for a given class.
   */
  def forClass(klass: Class[_]) = new Log(klass, Logger.getLogger(klass.getCanonicalName))

  protected val IgnoredFiles = Set("Log.scala")
  protected val IgnoredMethods = classOf[Log].getMethods.map { _.getName }.toSet
}

/**
 * In general, use the Logging trait rather than using Log directly.
 *
 * A wrapper for java.util.logging.Logger which allows for a smoother
 * interaction with Scala classes. All messages are treated as format strings:
 *
 *    log.info("We have this many threads: %d", thread.size)
 *
 * Each log level has two methods: one for logging regular messages, the other
 * for logging messages with thrown exceptions.
 *
 * The log levels here are those of java.util.logging.Level, and while they are
 * slightly insane (CONFIG? really?) I'd rather preserve the mapping than try
 * to figure out what other library authors mean when they use a particular
 * level (or use slf4j's mappings, which further muddy the waters).
 *
 * @author coda
 */
class Log(private val klass: Class[_], val logger: Logger) {
  
  /**
   * Logs a message with optional parameters at level FINEST.
   */
  def finest(message: String, params: Any*) {
    log(Level.FINEST, None, message, params)
  }

  /**
   * Logs a thrown exception and a message with optional parameters at level
   * FINEST.
   */
  def finest(thrown: Throwable, message: String, params: Any*) {
    log(Level.FINEST, Some(thrown), message, params)
  }

  /**
   * Logs a message with optional parameters at level FINER.
   */
  def finer(message: String, params: Any*) {
    log(Level.FINER, None, message, params)
  }

  /**
   * Logs a thrown exception and a message with optional parameters at level
   * FINER.
   */
  def finer(thrown: Throwable, message: String, params: Any*) {
    log(Level.FINER, Some(thrown), message, params)
  }

  /**
   * Logs a message with optional parameters at level FINE.
   */
  def fine(message: String, params: Any*) {
    log(Level.FINE, None, message, params)
  }

  /**
   * Logs a thrown exception and a message with optional parameters at level
   * FINE.
   */
  def fine(thrown: Throwable, message: String, params: Any*) {
    log(Level.FINE, Some(thrown), message, params)
  }

  /**
   * Logs a message with optional parameters at level CONFIG.
   */
  def config(message: String, params: Any*) {
    log(Level.CONFIG, None, message, params)
  }

  /**
   * Logs a thrown exception and a message with optional parameters at level
   * CONFIG.
   */
  def config(thrown: Throwable, message: String, params: Any*) {
    log(Level.CONFIG, Some(thrown), message, params)
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
   * Logs a message with optional parameters at level WARNING.
   */
  def warning(message: String, params: Any*) {
    log(Level.WARNING, None, message, params)
  }

  /**
   * Logs a thrown exception and a message with optional parameters at level
   * WARNING.
   */
  def warning(thrown: Throwable, message: String, params: Any*) {
    log(Level.WARNING, Some(thrown), message, params)
  }

  /**
   * Logs a message with optional parameters at level SEVERE.
   */
  def severe(message: String, params: Any*) {
    log(Level.SEVERE, None, message, params)
  }

  /**
   * Logs a thrown exception and a message with optional parameters at level
   * SEVERE.
   */
  def severe(thrown: Throwable, message: String, params: Any*) {
    log(Level.SEVERE, Some(thrown), message, params)
  }

  private def log(level: Level, thrown: Option[Throwable], message: String, values: Seq[Any]) = if (logger.isLoggable(level)) {
    logger.log(buildRecord(level, message, thrown, values))
  }

  private def buildRecord(level: Level, message: String, exception: Option[Throwable], values: Seq[Any]) = {
    val record = new LogRecord(level, message.format(values:_*))
    record.setSourceClassName(klass.getCanonicalName)
    val stack = (new Throwable()).getStackTrace.dropWhile { f =>
      Log.IgnoredFiles.contains(f.getFileName) || Log.IgnoredMethods.contains(f.getMethodName)
    }.head
    record.setSourceMethodName(stack.getMethodName)
    exception.map { record.setThrown(_) }
    record
  }
}
