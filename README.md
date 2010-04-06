Logula
======

*Bwah ah ah ah.*

[Logula](http://github.com/codahale/logula) is a Scala library which provides a
sane log output format and an easy-to-use mixin for adding logging to your code.

It's based on the `java.util.logging` classes because... well, because they're
there.


Requirements
------------

* Java SE 6
* Scala 2.8 Beta1


How To Use
----------

**First**, specify Logula as a dependency:

    val codaRepo = "Coda Hale's Repository" at "http://repo.codahale.com/"
    val logula = "com.codahale" %% "logula" % "1.0.0" withSources()

**Second**, configure the logging system:
    
    import com.codahale.logula.Logging
    import java.util.logging.Level
    
    Logging.configure(Level.INFO, "com.myproject.weebits" -> Level.FINE)

**Third**, add some logging to your classes:
    
    class MyThing extends Logging {
      def complicatedManoeuvre() {
        log.warning("This is about to get complicated...")
        // complicated bit elided
        try {
          log.info("Trying to do %d backflips", backflipsToAttempt)
        } catch {
          case e: Exception => log.severe(e, "Horrible things have happened.")
        }
      }
    }


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

    FINEST  [2010-04-06 06:42:35.271] com.codahale.logula.examples.ThingDoer#run: Contemplating doing a thing.
    FINER   [2010-04-06 06:42:35.274] com.codahale.logula.examples.ThingDoer#run: About to do a thing.
    FINE    [2010-04-06 06:42:35.274] com.codahale.logula.examples.ThingDoer#run: Commencing to do a thing.
    CONFIG  [2010-04-06 06:42:35.274] com.codahale.logula.examples.ThingDoer#run: Doing a thing 1 time(s)
    INFO    [2010-04-06 06:42:35.275] com.codahale.logula.examples.ThingDoer#run: Doing a thing
    WARNING [2010-04-06 06:42:35.275] com.codahale.logula.examples.ThingDoer#run: This may get ugly.
    SEVERE  [2010-04-06 06:42:35.275] com.codahale.logula.examples.ThingDoer#run: The thing has gone horribly wrong.
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

  * All timestamps are in UTC. This really should be OK with you.
  * You can grep for messages of a specific level really easily:
    `tail -f logula.log | grep "^WARNING"`
  * You can grep for messages from a specific class or package really easily:
    `tail -f logula.log | grep "ThingDoer"`
  * You can even pull out full exception stack traces, plus the accompanying
    log message: `tail -f logula.log | grep -B 1 "^\!"`
  * If you squint, you can still make out the actual log messages.

License
-------

Copyright (c) 2010 Coda Hale
Published under The MIT License, see LICENSE