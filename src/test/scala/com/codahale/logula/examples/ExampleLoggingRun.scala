package com.codahale.logula.examples

import com.codahale.logula.Logging
import java.util.logging.Level

class ThingDoer extends Logging {
  def run() {
    log.finest("Contemplating doing a thing.")
    log.finer("About to do a thing.")
    log.fine("Commencing to do a thing.")
    log.config("Doing a thing %d time(s)", 1)
    log.info("Doing a thing")
    log.warning("This may get ugly.")

    try {
      error("oh noes!")
    } catch {
      case e: Exception => log.severe(e, "The thing has gone horribly wrong.")
    }
    
    log.info("Just kidding!")
  }
}

class SilencedRunner extends Logging {
  def run() {
    log.warning("BLAH BLAH BLAH BLAH BLAH BLAH BLAH")
    log.warning("BLAH BLAH BLAH BLAH BLAH BLAH BLAH")
    log.warning("BLAH BLAH BLAH BLAH BLAH BLAH BLAH")
    log.warning("BLAH BLAH BLAH BLAH BLAH BLAH BLAH")
    log.warning("BLAH BLAH BLAH BLAH BLAH BLAH BLAH")
    log.warning("BLAH BLAH BLAH BLAH BLAH BLAH BLAH")
  }
}

object ExampleLoggingRun extends Logging {
  def main(args: Array[String]) {
    Logging.configure(Level.ALL,
      "com.codahale.logula.examples.SilencedRunner" -> Level.OFF)
    new ThingDoer().run()
    new SilencedRunner().run()
  }
}
