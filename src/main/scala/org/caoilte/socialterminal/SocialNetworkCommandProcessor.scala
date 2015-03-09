package org.caoilte.socialterminal

import org.joda.time.{DateTime}
import scalaz.{\/-, \/}


sealed trait Command {
  def userName:String
}
case class ReadCommand(userName:String, timeNow:DateTime) extends Command
case class WallCommand(userName:String, timeNow:DateTime) extends Command
case class FollowCommand(userName:String, otherUserName:String) extends Command
case class PostCommand(userName:String, post:String, timeNow:DateTime) extends Command


class SocialNetworkCommandProcessor extends SocialMessageFormatter {
  val network = new SocialNetwork()

  def handleCommand(command: Command):List[String] = {
    userOrError(command).flatMap(handleUserAndCommand(_, command)).valueOr(List(_))
  }

  private def userOrError(command: Command):String\/User = command match {
    case PostCommand(userName, _, _) => \/-(network.userOrNewUser(userName))
    case otherCommand => network.userOrError(otherCommand.userName)
  }

  private def handleUserAndCommand(user:User, command:Command): String\/List[String] = {
    command match {
      case PostCommand(_, message, timeNow) => {
        user.post(message, timeNow)
        \/-(Nil)
      }
      case ReadCommand(_, timeNow) => {
        \/-(prettyFormatEvents(user.timeline(), false, timeNow))
      }
      case WallCommand(_, timeNow) => {
        \/-(prettyFormatEvents(user.wall(), true, timeNow))
      }
      case FollowCommand(_, otherUserName) => {
        network.userOrError(otherUserName).map {
          otherUser => {
            user.follow(otherUser)
            Nil
          }
        }
      }
    }
  }
}