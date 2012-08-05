package com.codahale.logula

import org.apache.log4j.spi.LoggingEvent
import org.apache.log4j.EnhancedPatternLayout

/**
 * A single-line formatter which logs messages in the following format:
 * <pre>
 *  [yyyy-MM-dd HH:mm:ss,SSS] LoggerName: This is the message
 * </pre>
 * Timestamps are always in UTC.
 *
 * Associated exception stack traces are logged on the following lines, preceded
 * by an exclamation point. This allows for easy mechanical extraction of stack
 * traces from log files via standard tools like grep
 * (e.g., {@code tail -f logula.log | grep '^\!' -B 1}).
 */
abstract class BaseFormatter(format: String) extends EnhancedPatternLayout(format) {

  override def format(event: LoggingEvent) = {
    val msg = new StringBuilder
    msg.append(beforeMsg(event))
    msg.append(super.format(event))
    if (event.getThrowableInformation != null) {
      for (line <- event.getThrowableInformation.getThrowableStrRep) {
        msg.append("! ")
        msg.append(line)
        msg.append("\n")
      }
    }
    msg.toString
  }

  def beforeMsg(event: LoggingEvent) = ""

  override def ignoresThrowable = false
}
