package pl.tictactoe.core

import cats.syntax.all._
import pl.tictactoe.utils.Helpers
import pl.tictactoe.utils.Globals

object CoordinateParser {

  private val Letters = LazyList.from('A').take(Globals.MaxBoardSize)

  private def fromLetter(c: Char): Option[Int] = Letters.indexOf(c.toInt) match {
    case -1 => None
    case x => Some(x)
  }

  private def parseInt(s: String) = Helpers.parseInt(1, Globals.MaxBoardSize)(s).map(_ - 1)

  def parse(s: String): Option[Coordinate] = s.replaceAll("\\s+", "") match {
    case m if m.headOption.exists(_.isLetter) => (fromLetter(m.head), parseInt(m.drop(1))).mapN(Coordinate.apply)
    case m if m.lastOption.exists(_.isLetter) => (fromLetter(m.last), parseInt(m.dropRight(1))).mapN(Coordinate.apply)
    case _ => None
  }

}
