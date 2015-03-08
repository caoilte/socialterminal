package org.caoilte.socialterminal

import org.joda.time.DateTime
import org.scalatest.{Matchers, FlatSpec}

class SocialNetworkCommandProcessorTests extends FlatSpec with Matchers {

  val FIVE_MINUTES_AGO          = new DateTime(2015, 1, 1, 7, 10, 0)
  val TWO_MINUTES_AGO           = new DateTime(2015, 1, 1, 7, 13, 0)
  val ONE_MINUTE_AGO            = new DateTime(2015, 1, 1, 7, 14, 0)
  val TWO_SECONDS_AGO           = new DateTime(2015, 1, 1, 7, 14, 58)
  val NOW                       = new DateTime(2015, 1, 1, 7, 15, 0)
  val THIRTEEN_SECONDS_FROM_NOW = new DateTime(2015, 1, 1, 7, 15, 13)

  it should "handle the three scenarios" in {
    val processor = new SocialNetworkCommandProcessor
    processor.handleCommand(PostCommand("Alice", "I love the weather today", FIVE_MINUTES_AGO)) should
      equal(Nil)
    processor.handleCommand(PostCommand("Bob", "Damn! We lost!", TWO_MINUTES_AGO)) should
      equal(Nil)
    processor.handleCommand(PostCommand("Bob", "Good game though.", ONE_MINUTE_AGO)) should
      equal(Nil)
    processor.handleCommand(ReadCommand("Alice", NOW)) should
      equal(List(
        "I love the weather today (5 minutes ago)"
      ))
    processor.handleCommand(ReadCommand("Bob", NOW)) should
      equal(List(
        "Good game though. (1 minute ago)",
        "Damn! We lost! (2 minutes ago)"
      ))
    processor.handleCommand(PostCommand("Charlie", "I'm in New York today! Anyone want to have a coffee?", TWO_SECONDS_AGO)) should
      equal(Nil)
    processor.handleCommand(FollowCommand("Charlie", "Alice")) should equal(Nil)
    processor.handleCommand(WallCommand("Charlie", NOW)) should
      equal(List(
        "Charlie - I'm in New York today! Anyone want to have a coffee? (2 seconds ago)",
        "Alice - I love the weather today (5 minutes ago)"
      ))
    processor.handleCommand(FollowCommand("Charlie", "Bob")) should equal(Nil)
    // You can fix the final assertion by changing the 'wants' to 'want'
    processor.handleCommand(WallCommand("Charlie", THIRTEEN_SECONDS_FROM_NOW)) should
      equal(List(
        "Charlie - I'm in New York today! Anyone wants to have a coffee? (15 seconds ago)",
        "Bob - Good game though. (1 minute ago)",
        "Bob - Damn! We lost! (2 minutes ago)",
        "Alice - I love the weather today (5 minutes ago)"
      ))

  }

}
