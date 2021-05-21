package pl.tictactoe.core

import cats.syntax.all._

enum BoardError(msg: String) extends Throwable(msg):
  case FieldAlreadyTaken(c: Coordinate, p: Player) extends BoardError(show"The field $c is already taken by $p.")
  case WrongPlayer(c: Coordinate, p: Player) extends BoardError(show"Wrong player: $p.")
  case CoordinateOutOfBound(c: Coordinate) extends BoardError(show"Coordinates are out of bounds: $c (${c.x}, ${c.y}).")
  case GameIsOver(winner: Option[Player]) extends BoardError(winner.fold("Game was drawn")(p => show"Game was already won by $p."))
  case InvalidConfiguration(msg: String) extends BoardError(msg)

