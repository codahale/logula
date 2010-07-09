package com.codahale.logula

import java.text.{FieldPosition, SimpleDateFormat}
import java.util.{TimeZone, Date}
import java.util.logging.{LogRecord, Formatter => JdkFormatter}
import java.io.{PrintWriter, StringWriter}

object Formatter {
  protected val DateFormat = " [yyyy-MM-dd HH:mm:ss.SSS] "
}

/**
 * A single-line formatter which logs messages in the following format:
 *
 *  LEVEL  [yyyy-MM-dd HH:mm:ss.SSS] SourceClass#sourceMethod: Message test
 *
 * Dates are always in UTC.
 *
 * Associated exception stack traces are logged on the following lines, preceded
 * by an exclamation point. This allows for easy mechanical extraction of stack
 * traces from log files via standard tools like grep
 * (e.g., tail -f logula.log | grep "^\!" -B 1).
 */
class Formatter extends JdkFormatter {
  import Formatter._

  def format(record: LogRecord) = {
    val buffer = new StringBuffer(100)
    buffer.append(record.getLevel.getName.padTo(7, " ").mkString)
    logTimestamp(record.getMillis, buffer)
    buffer.append(record.getSourceClassName)
    buffer.append("#")
    buffer.append(record.getSourceMethodName)
    buffer.append(": ")
    buffer.append(formatMessage(record))

    if (record.getThrown != null) {
      logStacktrace(record.getThrown, buffer)
    }

    buffer.append('\n')
    buffer.toString
  }

  private def logStacktrace(e: Throwable, buffer: StringBuffer) {
    buffer.append('\n')
    val output = new StringWriter
    e.printStackTrace(new PrintWriter(output))
    buffer.append("! ")
    buffer.append(output.toString.replaceAll("\n", "\n! ").replaceAll("\t", "  "))
  }

  private def logTimestamp(millis: Long, buffer: StringBuffer) {
    val format = new SimpleDateFormat(DateFormat)
    format.setTimeZone(TimeZone.getTimeZone("UTC"))
    format.format(new Date(millis), buffer, new FieldPosition(0))
  }
}
