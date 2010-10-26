package com.codahale.logula.tests

import com.codahale.simplespec.Spec
import org.apache.log4j.{Logger, Level}
import com.codahale.logula.{Log, Logging}

class LoggingExample extends Logging {
  def getLog = log
}

object LoggingSpec extends Spec {
  class `A class which extends Logging` {
    val example = new LoggingExample

    def `should have a Log instance` {
      example.getLog must haveClass[Log]
    }
  }
}
