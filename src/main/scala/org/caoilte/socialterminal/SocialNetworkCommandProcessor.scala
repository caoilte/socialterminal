package org.caoilte.socialterminal

import org.joda.time.{DateTime}
import scalaz.{\/-, \/}


sealed trait Command {
  def user:String
}
case class ReadCommand(user:String, timeNow:DateTime) extends Command
case class WallCommand(user:String, timeNow:DateTime) extends Command
case class FollowCommand(user:String, otherUser:String) extends Command
case class PostCommand(user:String, post:String, timeNow:DateTime) extends Command


class SocialNetworkCommandProcessor extends SocialMessageFormatter {
  val network = new SocialNetwork()

  def handleCommand(command: Command):List[String] = {
    userOrError(command).flatMap(handleUserAndCommand(_, command)).valueOr(List(_))
  }

  private def userOrError(command: Command):String\/User = command match {
    case PostCommand(user, _, _) => \/-(network.userOrNewUser(user))
    case otherCommand => network.userOrError(otherCommand.user)
  }

  private def handleUserAndCommand(user:User, command:Command): String\/List[String] = {
    command match {
      case PostCommand(user, message, timeNow) => {
        network.userOrNewUser(user).post(message, timeNow)
        \/-(Nil)
      }
      case ReadCommand(_, timeNow) => {
        \/-(prettyFormatEvents(user.timeline(), false, timeNow))
      }
      case WallCommand(_, timeNow) => {
        \/-(prettyFormatEvents(user.wall(), true, timeNow))
      }
      case FollowCommand(_, otherName) => {
        network.userOrError(otherName).map {
          otherUser => {
            user.follow(otherUser)
            Nil
          }
        }
      }
    }
  }
}