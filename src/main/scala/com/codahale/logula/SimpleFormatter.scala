package com.codahale.logula

import org.apache.log4j.spi.LoggingEvent

/**
 * A single-line formatter which logs messages in the following format:
 *
 * <pre>
 *  [L] [yyyy-MM-dd HH:mm:ss,SSS] This is the message
 * </pre>
 * 
 * L is the first letter of the logging level
 */
class SimpleFormatter extends BaseFormatter("[%d{ISO8601}{UTC}] %m\n") {
  override def beforeMsg(event: LoggingEvent) = {
    "[" + event.getLevel.toString.charAt(0) + "]"
  }
}
