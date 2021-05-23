package pl.tictactoe.core

import cats.syntax.all._

class CoordinateSpec extends munit.FunSuite:

  test("should correctly format coordinates") {
    //given
    val cases = List(
      Coordinate(0,0) -> "A1",
      Coordinate(10,12) -> "K13",
      Coordinate(15,3) -> "P4",
    )

    //then
    cases.foreach {
      case (coordinate, expected) =>
        assert(clue(coordinate.show) == expected)
    }

  }
