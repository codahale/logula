package com.codahale.logula.tests

import com.codahale.simplespec.Spec
import com.codahale.logula.{Log, Logging}

class LoggingExample extends Logging {
  def getLog = log
}

object LoggingSpec extends Spec {
  class `A class which extends Logging` {
    private val example = new LoggingExample

    def `has a Log instance` = {
      example.getLog must haveClass[Log]
    }
  }
}
