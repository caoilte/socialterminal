package org.caoilte.socialterminal

import org.joda.time.DateTime
import org.scalatest.{Matchers, FlatSpec}

class SocialMessageFormatterTests extends FlatSpec with SocialMessageFormatter with Matchers {

  val A_DATE_TIME                   = new DateTime(2015, 1, 1, 10, 15, 0)
  val SECONDS_BEFORE_A_DATE_TIME    = new DateTime(2015, 1, 1, 10, 14, 45)
  val ONE_MINUTE_BEFORE_A_DATE_TIME = new DateTime(2015, 1, 1, 10, 14, 0)
  val MINUTES_BEFORE_A_DATE_TIME    = new DateTime(2015, 1, 1, 10, 10, 45)
  val ONE_HOUR_BEFORE_A_DATE_TIME   = new DateTime(2015, 1, 1, 9, 15, 0)
  val HOURS_BEFORE_A_DATE_TIME      = new DateTime(2015, 1, 1, 7, 10, 45)

  behavior of "Social Message Formatter Pretty Period String Calculator"

  it should "should print timeStamps over an hour with just hours" in {
    prettyPeriodString(A_DATE_TIME, HOURS_BEFORE_A_DATE_TIME) should equal("3 hours")
  }

  it should "should print timeStamps exactly an hour with just one hour" in {
    prettyPeriodString(A_DATE_TIME, ONE_HOUR_BEFORE_A_DATE_TIME) should equal("1 hour")
  }

  it should "should print timeStamps under an hour with just minutes" in {
    prettyPeriodString(A_DATE_TIME, MINUTES_BEFORE_A_DATE_TIME) should equal("4 minutes")
  }

  it should "should print timeStamps exactly one minute with just one minute" in {
    prettyPeriodString(A_DATE_TIME, ONE_MINUTE_BEFORE_A_DATE_TIME) should equal("1 minute")
  }

  it should "should print timeStamps under a minute with just seconds" in {
    prettyPeriodString(A_DATE_TIME, SECONDS_BEFORE_A_DATE_TIME) should equal("15 seconds")
  }
}
