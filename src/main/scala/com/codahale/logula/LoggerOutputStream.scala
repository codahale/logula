package com.codahale.logula

import org.apache.log4j.{Level, Logger}
import java.io.ByteArrayOutputStream

/**
 * An {@link java.util.OutputStream} implementation which writes whole lines to
 * a given {@link org.apache.log4j.Logger} at a given
 * {@link org.apache.log4j.Level} when flushed.
 *
 * @author coda
 */
class LoggerOutputStream(logger: Logger, level: Level) extends ByteArrayOutputStream {
  override def flush {
    val lines: Seq[String] = synchronized {
      super.flush()
      val output = toString
      super.reset()
      if (!output.trim.isEmpty) {
        var lines = output.split('\n')
        if (!output.endsWith("\n")) { // partial line
          write(lines.last.getBytes)
          lines = lines.slice(0, lines.size-1)
        }
        lines
      } else Seq.empty
    }
    for (line <- lines) {
      logger.log(level, line)
    }
  }
}
