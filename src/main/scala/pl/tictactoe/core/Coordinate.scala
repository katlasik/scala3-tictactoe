package pl.tictactoe.core

import cats.Show
import pl.tictactoe.utils.Globals
import cats.syntax.all._

case class Coordinate(x: Int, y: Int)

object Coordinate:
    private val Letters = LazyList.from('A').take(Globals.MaxBoardSize)

    given Show[Coordinate] = Show.show(c => s"${Letters.get(c.x).fold("")(_.toChar)}${c.y+1}")

