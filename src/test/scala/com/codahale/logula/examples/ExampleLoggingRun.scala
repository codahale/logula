package com.codahale.logula.examples

import com.codahale.logula.Logging
import org.apache.log4j.Level

class ThingDoer extends Logging {
  def run() {
    log.trace("Contemplating doing a thing.")
    log.debug("About to do a thing.")
    log.info("Doing a thing")
    log.warn("This may get ugly.")

    try {
      error("oh noes!")
    } catch {
      case e: Exception => log.error(e, "The thing has gone horribly wrong.")
    }
    
    log.fatal("Just kidding!")
  }
}

class SilencedRunner extends Logging {
  def run() {
    log.warn("BLAH BLAH BLAH BLAH BLAH BLAH BLAH")
    log.warn("BLAH BLAH BLAH BLAH BLAH BLAH BLAH")
    log.warn("BLAH BLAH BLAH BLAH BLAH BLAH BLAH")
    log.warn("BLAH BLAH BLAH BLAH BLAH BLAH BLAH")
    log.warn("BLAH BLAH BLAH BLAH BLAH BLAH BLAH")
    log.warn("BLAH BLAH BLAH BLAH BLAH BLAH BLAH")
  }
}

object ExampleLoggingRun extends Logging {
  def main(args: Array[String]) {
    Logging.configure { log =>
      log.registerWithJMX = true

      log.level = Level.TRACE

      log.loggers("com.codahale.logula.examples.SilencedRunner") = Level.OFF

      log.console.enabled = true
      log.console.threshold = Level.ALL

      log.file.enabled = true
      log.file.filenamePattern = "./logs/%d{yyyy-MM-dd}.log.gz"
      log.file.threshold = Level.INFO
    }

    new ThingDoer().run()
    new SilencedRunner().run()
  }
}
