v2.1.0: TBD
===================

* Removed garbage collection logging
* Dropped dependency on apache-log4j-extras
* Switched from time-based rolling of log file to simple maximum file size
* Added retention settings for log files

v2.0.0: Oct 28 2010
===================

* Moved from `java.util.logging` to **log4j**
* Improved `Log` interface
* Change logging levels via JMX
* Log to stdout or a rotating log file
* Log garbage collection times

v1.0.3: Jul 15 2010
===================

* Compiled exclusively for Scala 2.8.0 final

v1.0.2: Jul 09 2010
===================

* Cross-compiled against Scala 2.8.0.RC3, RC5, RC6, and RC7
* Strip tab characters from exception stack traces

v1.0.1: May 11 2010
===================

* Cross-compiled against Scala 2.8.0.RC2

v1.0.0: Apr 06 2010
===================

* Initial release