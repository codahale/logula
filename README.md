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
* Scala 2.8.0
* log4j 1.2


How To Use
----------

**First**, specify Logula as a dependency:

    val codaRepo = "Coda Hale's Repository" at "http://repo.codahale.com/"
    val logula = "com.codahale" %% "logula" % "2.0.0-SNAPSHOT" withSources()

You will also need a modern version of log4j and log4j-extras. SBT should
download them as transitive dependencies.

**Second**, configure the logging system:

You can log all messages to `stdout`:

    import com.codahale.logula.Logging
    import org.apache.log4j.Level
    
    Logging.configure { log =>
      log.registerWithJMX = true

      log.loggers("com.myproject.weebits") = Level.OFF

      log.console.enabled = true
      log.console.level = Level.ALL

      log.file.enabled = true
      log.file.filenamePattern = "/var/log/myapp/%d{yyyy-MM-dd}.log.gz"
      log.file.level = Level.INFO

      log.gc.enabled = true
      log.gc.checkEvery(1, TimeUnit.SECONDS)
      log.gc.addDurationThreshold(Level.DEBUG,    0, TimeUnit.MILLISECONDS)
      log.gc.addDurationThreshold(Level.INFO,   300, TimeUnit.MILLISECONDS)
      log.gc.addDurationThreshold(Level.WARN,  1000, TimeUnit.MILLISECONDS)
    }

This will log all messages on Oct 24, 2010 to both standard out and
`/var/log/myapp/2010-10-24.log`, and when the server rolls over to a new log,
the old log will be gzipped. (You can also use ZIP compression by making the
filename pattern end in `.zip` or disable compression entirely by not naming
your log files `.gz` or `.zip`.)

The `%d{yyyy-MM-dd}` format documentation can be found by looking at
`java.util.SimpleDateFormat`.

If `log.gc.enabled` is `true`, Logula will log information about the JVM's
garbage collection:
    
    DEBUG [2010-10-25 22:08:24,534] GC: ParNew: 0.014s, 15.92MB reclaimed -> 25.15MB/811.88MB used
    DEBUG [2010-10-25 22:08:24,542] GC: ConcurrentMarkSweep: 0.096s, 1.99MB reclaimed -> 23.44MB/811.88MB used
    DEBUG [2010-10-25 22:08:25,428] GC: ParNew: 0.002s, 16.64MB reclaimed -> 28.05MB/811.88MB used

You can specify thresholds at which GC runs are logged at particular levels. By
default, Logula logs all GC runs which take 0ms to 99ms at `TRACE`, 100ms to
499ms at `DEBUG`, 500ms to 999ms at `INFO`, and ≥1000ms at `WARN`.

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
    `tail -f logula.log | grep "^WARN"`
  * You can grep for messages from a specific class or package really easily:
    `tail -f logula.log | grep "ThingDoer"`
  * You can even pull out full exception stack traces, plus the accompanying
    log message: `tail -f logula.log | grep -B 1 "^\!"`
  * If you squint, you can still make out the actual log messages.

License
-------

Copyright (c) 2010 Coda Hale

Published under The MIT License, see LICENSE