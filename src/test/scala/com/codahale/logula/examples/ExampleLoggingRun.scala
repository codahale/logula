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
    Logging.logToConsole(Level.ALL,
      "com.codahale.logula.examples.SilencedRunner" -> Level.OFF)

    Logging.redirectStdErr(Level.ERROR)
    Logging.redirectStdOut(Level.INFO)

    println("stdout woo ")
    System.err.print("one ")
    System.err.print("two ")
    System.err.flush()
    System.err.println("three!")

    new ThingDoer().run()
    new SilencedRunner().run()

    Logging.restoreStdErr()
    Logging.restoreStdOut()
  }
}
