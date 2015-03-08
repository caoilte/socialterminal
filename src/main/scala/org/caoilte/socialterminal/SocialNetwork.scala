package org.caoilte.socialterminal

import org.joda.time.DateTime

import scalaz.\/
import scalaz._
import std.option._

case class SocialMessage(sender:User, message: String, timeStamp:DateTime)

case class TimeLine(private var subscribers:List[Wall], private var events: List[SocialMessage] = Nil) {
  def post(event: SocialMessage) {
    subscribers.foreach(_.post(event))
    events = event :: events
  }

  def addSubscriber(subscriber:Wall) {
    events.foreach(subscriber.post)
    subscribers = subscriber :: subscribers
  }

  def apply():List[SocialMessage] = events
}


case class Wall(private var events: List[SocialMessage] = Nil) {
  def post(event: SocialMessage) {
    events = event :: events
  }

  def apply():List[SocialMessage] = events
}


object User {
  def apply(name:String):User = {
    val wall = new Wall()
    val timeline = new TimeLine(List(wall))
    User(name, timeline, wall)
  }
}

case class User(name:String, timeline: TimeLine, wall:Wall) {
  def post(message:String, time:DateTime) {
    timeline.post(SocialMessage(this, message, time))
  }

  def follow(user:User) {
    user.timeline.addSubscriber(wall)
  }
}

class SocialNetwork(private var users:Map[String, User] = Map[String, User]()) {
  def userOrError(name:String):String\/User = {
    toRight(users.get(name))(s"No such user '$name'")
  }
  def userOrNewUser(name:String):User = {
    users.get(name) match {
      case Some(user) => user
      case None => {
        val user = User(name)
        users += (name -> user)
        user
      }
    }
  }
}