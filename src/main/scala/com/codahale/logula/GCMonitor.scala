package com.codahale.logula

import java.util.{Map => JMap}
import scala.collection.JavaConversions._
import org.apache.log4j.{Level, Logger}
import javax.management.ObjectName
import management.{ManagementFactory, MemoryUsage}

/**
 * A runnable which will output JVM garbage collection to a logger.
 *
 * Currently only works with the Oracle/Sun JVM.
 *
 * @author coda
 */
class GCMonitor(logger: Logger, durations: Seq[(Level, Long)]) extends Runnable {
  private val minDurations = durations.sortBy { _._2 }.reverse
  private var timings = Map[String, Long]()

  private val beans: Seq[Object] = try {
    Class.forName("com.sun.management.GcInfo")
    val server = ManagementFactory.getPlatformMBeanServer
    val gcBeanClass = Class.forName("com.sun.management.GarbageCollectorMXBean")
    val gcName = new ObjectName(ManagementFactory.GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE + ",*")

    server.queryNames(gcName, null).map { name =>
      ManagementFactory.newPlatformMXBeanProxy(
        server, name.getCanonicalName, gcBeanClass
      ).asInstanceOf[Object]
    }.toSeq
  } catch {
    case e: ClassNotFoundException =>
      logger.warn("Cannot load Oracle/Sun JVM GC monitoring classes")
      Seq.empty
  }

  def run {
    try {
      for (bean <- beans) {
        val getGcInfo = bean.getClass.getDeclaredMethod("getLastGcInfo")
        val lastGcInfo: Object = getGcInfo.invoke(bean)
        if (lastGcInfo != null) {
          val usageBeforeGc = lastGcInfo.getClass.getDeclaredMethod("getMemoryUsageBeforeGc").invoke(lastGcInfo).asInstanceOf[JMap[String, MemoryUsage]]
          val usageAfterGc = lastGcInfo.getClass.getDeclaredMethod("getMemoryUsageAfterGc").invoke(lastGcInfo).asInstanceOf[JMap[String, MemoryUsage]]
          val duration = lastGcInfo.getClass.getDeclaredMethod("getDuration").invoke(lastGcInfo).asInstanceOf[Long]
          val name = bean.getClass.getDeclaredMethod("getName").invoke(bean).asInstanceOf[String]
          val timestamp = bean.getClass.getDeclaredMethod("getCollectionTime").invoke(bean).asInstanceOf[Long]

          if (timings.get(name).map { t => t < timestamp }.getOrElse(true)) {
            timings += (name -> timestamp)

            val previousMemoryUsed = usageBeforeGc.valuesIterator.map { _.getUsed }.sum
            val memoryUsed = usageAfterGc.valuesIterator.map { _.getUsed }.sum
            val memoryMax = usageAfterGc.valuesIterator.map { _.getMax }.sum

            val level = minDurations.find { _._2 < duration }.map { _._1 }.getOrElse(Level.TRACE)
            if (logger.isEnabledFor(level)) {
              val time = duration / 1000.0
              val reclaimed = (previousMemoryUsed - memoryUsed) / 1024.0 / 1024.0
              val used = memoryUsed / 1024.0 / 1024.0
              val max = memoryMax / 1024.0 / 1024.0

              logger.log(level, "%s: %2.3fs, %2.2fMB reclaimed -> %2.2fMB/%2.2fMB used".format(
                name, time, reclaimed, used, max
              ))
            }
          }
        }
      }
    } catch {
      case e: Exception => logger.debug("Error reading GC activity", e)
    }
  }
}
