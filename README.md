A SINGLE PROCESS COMMAND LINE SOCIAL NETWORK
============================================

This project was written with Scala and SBT (Simple Build Tool).

It uses the [JLine] to read user input and [scala-parser-combinators] to parse it.

In order to build and run the application you simply need to install SBT as instructed [here][installingSBT]

RUNNING THE APPLICATION
=======================

Because of a bug you will need to fix one of the tests before you can run the application.

The working application can be run from the root directory of the project with the following command

    sbt run

TODO
====

* Replace unit tests with Scalacheck property tests where appropriate
* Wrap JLine and time with an IO Monad



[JLine]: https://github.com/jline/jline2 "Jline"
[scala-parser-combinators]: https://github.com/scala/scala-parser-combinators "Scala Parser Combinators"
[installingSBT]: http://www.scala-sbt.org/release/tutorial/Setup.html "Installing SBT"
