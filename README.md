Logula
======

*Bwah ah ah ah.*

[Logula](http://github.com/codahale/logula) is a Scala library which provides a
sane log output format and an easy-to-use mixin for adding logging to your code.

It's a thin front-end for [log4j 1.2](http://logging.apache.org/log4j/1.2/)
because `java.util.logging` was a pain in the neck to deal with.


Requirements
------------

* Java SE 6
* Scala 2.8.1
* log4j 1.2


How To Use
----------

**First**, specify Logula as a dependency:

    val codaRepo = "Coda Hale's Repository" at "http://repo.codahale.com/"
    val logula = "com.codahale" %% "logula" % "2.1.0-SNAPSHOT" withSources()

You will also need a modern version of log4j. SBT should download them as 
transitive dependencies.

**Second**, configure the logging system:

    import com.codahale.logula.Logging
    import org.apache.log4j.Level
    
    Logging.configure { log =>
      log.registerWithJMX = true
      
      log.level = Level.INFO
      log.loggers("com.myproject.weebits") = Level.OFF
      
      log.console.enabled = true
      log.console.threshold = Level.WARN
      
      log.file.enabled = true
      log.file.filename = "/var/log/myapp/myapp.log"
      log.file.maxSize = 10 * 1024 // KB
      log.file.retainedFiles = 5 // keep five old logs around
    }

**Third**, add some logging to your classes:
    
    class MyThing extends Logging {
      def complicatedManoeuvre() {
        try {
          log.warn("This is about to get complicated...")
          log.info("Trying to do %d backflips", backflipsToAttempt)
          // complicated bit elided
        } catch {
          case e: Exception => log.error(e, "Horrible things have happened.")
        }
      }
    }

Notice that the logging statements use Scala's formatting syntax, and that
logged exceptions are passed as the first argument.


Statement Arguments
-------------------

Unlike a lot of Scala logging libraries, Logula doesn't use pass-by-name
semantics (e.g., `f: => A`) for its logging statements, which means two things:

1. The Scala compiler doesn't have to create one-off closure objects for each
   logging statement. This should reduce the amount of garbage collection
   pressure.
2. If your logging arguments are complex to create, that price will be paid
   regardless of whether or not the statement is logged.

For example:
    
    log.debug("A huge collection: %s", things.mkString(", "))

The `mkString` call will happen every time. To prevent this, either keep your
arguments simple:
    
    log.debug("A huge collection: %s", things)

or only conditionally log them:
    
    if (log.isDebugEnabled) {
      log.debug("A huge collection: %s", things.mkString(", "))
    }

In most cases, it's simple enough to just log basic values.


The Log Format
--------------

Logula's log format has a few specific goals.

* Be roughly human readable. You shouldn't need another program to make sense of
  your program.
* Be machine parsable. You shouldn't need another human to make sense of your
  program. (Shush.)
* Make it easy for sleepy ops folks to figure out why things are pear-shaped at
  o'dark-thirty using standard UNIXy tools like `tail`, `grep`, and `fortune`.

An example of logging output looks like this:

    TRACE [2010-04-06 06:42:35,271] com.codahale.logula.examples.ThingDoer: Contemplating doing a thing.
    DEBUG [2010-04-06 06:42:35,274] com.codahale.logula.examples.ThingDoer: About to do a thing.
    INFO  [2010-04-06 06:42:35,274] com.codahale.logula.examples.ThingDoer: Doing a thing
    WARN  [2010-04-06 06:42:35,275] com.codahale.logula.examples.ThingDoer: Doing a thing
    ERROR [2010-04-06 06:42:35,275] com.codahale.logula.examples.ThingDoer: This may get ugly.
    FATAL [2010-04-06 06:42:35,275] com.codahale.logula.examples.ThingDoer: The thing has gone horribly wrong.
    ! java.lang.RuntimeException: oh noes!
    ! 	at scala.Predef$.error(Predef.scala:74)
    ! 	at com.codahale.logula.examples.ThingDoer.run(ExampleLoggingRun.scala:16)
    ! 	at com.codahale.logula.examples.ExampleLoggingRun$.main(ExampleLoggingRun.scala:40)
    ! 	at com.codahale.logula.examples.ExampleLoggingRun.main(ExampleLoggingRun.scala)
    ! 	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    ! 	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
    ! 	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
    ! 	at java.lang.reflect.Method.invoke(Method.java:597)
    ! 	at sbt.Run.run0(Run.scala:60)
    ! 	at sbt.Run.execute$1(Run.scala:47)
    ! 	at sbt.Run$$anonfun$run$2.apply(Run.scala:50)
    ! 	at sbt.Run$$anonfun$run$2.apply(Run.scala:50)
    ! 	at sbt.TrapExit$.executeMain$1(TrapExit.scala:33)
    ! 	at sbt.TrapExit$$anon$1.run(TrapExit.scala:42)
    ! 

A few items of note:

  * All timestamps are in UTC and ISO 8601 format. This really should be OK with
    you.
  * You can grep for messages of a specific level really easily:
    `tail -f logula.log | grep '^WARN'`
  * You can grep for messages from a specific class or package really easily:
    `tail -f logula.log | grep 'ThingDoer'`
  * You can even pull out full exception stack traces, plus the accompanying
    log message: `tail -f logula.log | grep -B 1 '^\!'`
  * If you squint, you can still make out the actual log messages.

License
-------

Copyright (c) 2010 Coda Hale

Published under The MIT License, see LICENSE