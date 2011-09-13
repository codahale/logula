package com.codahale.logula.tests

import com.codahale.simplespec.Spec
import com.codahale.logula.{Log, Logging}
import org.junit.Test

class LoggingExample extends Logging {
  def getLog = log
}

class LoggingSpec extends Spec {
  class `A class which extends Logging` {
    val example = new LoggingExample

    @Test def `has a Log instance` = {
      example.getLog.must(beA[Log])
    }
  }
}
