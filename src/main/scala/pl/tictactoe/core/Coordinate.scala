package pl.tictactoe.core

import cats.Show

case class Coordinate(x: Int, y: Int)

object Coordinate:
    given Show[Coordinate] = Show.show(CoordinateFormatter.format)

