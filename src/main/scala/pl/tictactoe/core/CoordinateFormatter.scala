package pl.tictactoe.core

import cats.syntax.all._
import pl.tictactoe.utils.Globals


object CoordinateFormatter {

  private val Letters = LazyList.from('A').take(Globals.MaxBoardSize)

  def format(c: Coordinate): String = 
    s"${Letters.get(c.x).fold("")(_.toChar)}${c.y+1}"

}
