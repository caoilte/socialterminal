package org.caoilte.socialterminal

import java.io.{FileDescriptor, FileInputStream}

import jline.console._
import jline.console.completer._
import org.joda.time.DateTime
import scala.util.parsing.combinator._




object Main extends RegexParsers {

  def word: Parser[String]    = """[A-Za-z]+""".r ^^ { _.toString }
  def any: Parser[String]    = """.+""".r ^^ { _.toString }
  def postingCommand: Parser[String]    = """->""".r ~> any
  def followingCommand: Parser[String]    = """follows""".r ~> word
  def wallCommand: Parser[String]    = """wall""".r

  def commands: Parser[Command] =
    word ~ postingCommand ^^ { case user ~ post => PostCommand(user, post, new DateTime())} |
    word ~ followingCommand ^^ { case user ~ otherUser => FollowCommand(user, otherUser)} |
    word <~ wallCommand ^^ { case user => WallCommand(user, new DateTime())} |
    word ^^ { case user => ReadCommand(user, new DateTime())}

  def main(args: Array[String])  {
    val reader = new ConsoleReader()
    reader.setBellEnabled(false)

    val ui = new SocialNetworkCommandProcessor()

    var line:String = ""

    try {
      do {
        line = readLine(reader)
        if (line != null) {
          val res = parse(commands, line)
          match {
            case Success(matched,_) => ui.handleCommand(matched).foreach(println(_))
            case Failure(msg,_) => println("FAILURE: " + msg)
            case Error(msg,_) => println("ERROR: " + msg)
          }
        }
      } while (line != null)
    } finally {
      reader.shutdown()
    }
  }

  def readLine(reader: ConsoleReader): String = {
    val line = reader.readLine("> ")
    if (line != null) line.trim() else null
  }
}
