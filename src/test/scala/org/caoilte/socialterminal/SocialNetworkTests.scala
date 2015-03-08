package org.caoilte.socialterminal

import org.joda.time.DateTime
import org.scalatest.{Matchers, FlatSpec}

import scalaz.-\/

class SocialNetworkTests extends FlatSpec with Matchers {

  val A_NAME = "Alice"
  val A_USER = User(A_NAME)
  val A_2ND_USER = User("Bob")
  val A_MESSAGE = "A MESSAGE"
  val A_2ND_MESSAGE = "A MESSAGE"
  val A_DATE_TIME = new DateTime(2015, 1, 1, 10, 15, 0)
  val A_SOCIAL_MESSAGE = SocialMessage(A_USER, A_MESSAGE, A_DATE_TIME)
  val A_2ND_SOCIAL_MESSAGE = SocialMessage(A_USER, A_2ND_MESSAGE, A_DATE_TIME)

  behavior of "A Timeline"

  it should "Retain all posted messages" in {
    val timeLine = new TimeLine(List())
    timeLine.post(A_SOCIAL_MESSAGE)
    timeLine() should equal(List(A_SOCIAL_MESSAGE))
    timeLine.post(A_2ND_SOCIAL_MESSAGE)
    timeLine() should equal(List(A_2ND_SOCIAL_MESSAGE, A_SOCIAL_MESSAGE))
    timeLine.addSubscriber(new Wall())
    timeLine.post(A_2ND_SOCIAL_MESSAGE)
    timeLine() should equal(List(A_2ND_SOCIAL_MESSAGE, A_2ND_SOCIAL_MESSAGE, A_SOCIAL_MESSAGE))
  }

  it should "Post a new message to all subscribers" in {
    val subscriber1 = new Wall()
    val subscriber2 = new Wall()
    val timeLine = new TimeLine(List(subscriber1, subscriber2))
    timeLine.post(A_SOCIAL_MESSAGE)
    subscriber1() should equal(List(A_SOCIAL_MESSAGE))
    subscriber2() should equal(List(A_SOCIAL_MESSAGE))
  }

  it should "Post all historical messages to new a subscriber" in {
    val timeLine = new TimeLine(List())
    timeLine.post(A_SOCIAL_MESSAGE)
    val subscriber1 = new Wall()
    timeLine.addSubscriber(subscriber1)
    subscriber1() should equal(List(A_SOCIAL_MESSAGE))
  }

  behavior of "A Social Network"

  it should "Error if asked to get or error on a user that does not exist" in {
    val network = new SocialNetwork()
    network.userOrError(A_NAME) should equal (-\/(s"No such user '$A_NAME'"))
  }

  it should "Return a new user if asked to get or create a user that does not exist" in {
    val network = new SocialNetwork()
    network.userOrNewUser(A_NAME) should equal (A_USER)
  }

  it should "Return an existing user if asked to get or create a user that already exists" in {
    val network = new SocialNetwork()
    val user1 = network.userOrNewUser(A_NAME)
    user1.post(A_MESSAGE, A_DATE_TIME)
    val user2 = network.userOrNewUser(A_NAME)
    user1 should equal (user2)
  }

  it should "Return an existing user if asked to get or error on a user that already exists" in {
    val network = new SocialNetwork()
    val user1 = network.userOrNewUser(A_NAME)
    user1.post(A_MESSAGE, A_DATE_TIME)
    val user2 = network.userOrError(A_NAME).toEither.right.get
    user1 should equal (user2)
  }

}
