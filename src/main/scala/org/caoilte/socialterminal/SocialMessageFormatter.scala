package org.caoilte.socialterminal

import org.joda.time.{Period, DateTime}
import org.joda.time.format.PeriodFormat

import scala.math.Ordering


trait SocialMessageFormatter {
  def prettyFormatEvents(socialMessages: List[SocialMessage], withUsers:Boolean, timeNow:DateTime): List[String] = {
    implicit val ordering = new Ordering[SocialMessage] {
      override def compare(x: SocialMessage, y: SocialMessage): Int = y.timeStamp.compareTo(x.timeStamp)
    }

    socialMessages.sorted map {
      event => {
        val prefix = if (withUsers) s"${event.sender.name} - " else ""

        s"$prefix${event.message} (${prettyPeriodString(timeNow, event.timeStamp)} ago)"
      }
    }
  }

  def prettyPeriodString(timeNow:DateTime, timeStamp:DateTime):String = {
    val fmt = PeriodFormat.getDefault

    val period = new Period(timeStamp, timeNow)
    if (period.getHours >= 1) fmt.print(period.toStandardHours)
    else if (period.getMinutes >= 1) fmt.print(period.toStandardMinutes)
    else fmt.print(period.toStandardSeconds)
  }
}
