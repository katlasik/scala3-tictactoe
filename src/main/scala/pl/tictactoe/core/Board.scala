package pl.tictactoe.core

import pl.tictactoe.utils.Globals
import cats.syntax.all._

case class BoardConfig(size: Int, toWin: Int)

final case class Board(config: BoardConfig, val fields: Fields, status: BoardStatus, moves: Int):

  import Board._

  def moveMultiple(moves: (Coordinate, Player)*): Either[BoardError, Board] =
    moves.foldLeft(this.asRight) {
      case (board, (coordinate, player)) => board.flatMap(_.move(coordinate, player))
    }

  def move(c: Coordinate, p: Player): Either[BoardError, Board] =

    status match {
      case BoardStatus.Won(winner) => BoardError.GameIsOver(winner.some).asLeft
      case BoardStatus.Drawn => BoardError.GameIsOver(none).asLeft
      case BoardStatus.Ongoing(nextPlayer) =>
        val maybeUpdated = fields(c).map {
          case FieldStatus.Empty =>
            if nextPlayer === p then
              fields.update(c, FieldStatus.Taken(p)).map(
                updated => 
                  checkVictory(copy(fields = updated, moves = moves + 1), p, c))
                    .toRight(BoardError.CoordinateOutOfBound(c)
                )
            else
              BoardError.WrongPlayer(c, p).asLeft
          case FieldStatus.Taken(_) => BoardError.FieldAlreadyTaken(c, p).asLeft
        }
        maybeUpdated.getOrElse(BoardError.CoordinateOutOfBound(c).asLeft)
    }



object Board:

  import Globals._

  private enum Translation(x: Int, y: Int):

    def translate(c: Coordinate, config: BoardConfig): Option[Coordinate] =
      val nx = c.x + x
      val ny = c.y + y

      Option.when(nx >= 0 && nx < config.size && ny >= 0 && ny < config.size)(Coordinate(nx, ny))

    case Up extends Translation(0, -1)
    case Down extends Translation(0, 1)
    case Left extends Translation(-1, 0)
    case Right extends Translation(1, 0)
    case UpLeft extends Translation(-1, -1)
    case DownLeft extends Translation(-1, 1)
    case UpRight extends Translation(1, -1)
    case DownRight extends Translation(1, 1)

  def create(boardSize: Int, toWin: Int): Either[BoardError.InvalidConfiguration, Board] =
    val fields = Fields.create(boardSize)
    Either.cond(
      boardSize >= MinBoardSize && boardSize <= MaxBoardSize && boardSize >= toWin && toWin >= MinBoardSize,
      Board(BoardConfig(boardSize, toWin), fields, BoardStatus.Ongoing(Player.X), 0),
      BoardError.InvalidConfiguration(s"Fields boardSize must be of size between $MinBoardSize and $MaxBoardSize and toWin must be equal or smaller than boardSize." +
        s" Received boardSize: $boardSize, toWin: $toWin.")
    )

  private def checkVictory(board: Board, player: Player, coordinate: Coordinate): Board = {
    def iterate(translation: Translation, c: Coordinate, n: Int): Int = {
      translation.translate(c, board.config).flatMap {
        next => board.fields(next).map{
          case FieldStatus.Taken(`player`) => iterate(translation, next, n + 1)
          case _ => n
        }
      }.getOrElse(n)
    }

    
    val victory = List(
      1 + iterate(Translation.Left, coordinate, 0) + iterate(Translation.Right, coordinate, 0),
      1 + iterate(Translation.UpLeft, coordinate, 0) + iterate(Translation.DownRight, coordinate, 0),
      1 + iterate(Translation.DownLeft, coordinate, 0) + iterate(Translation.UpRight, coordinate, 0),
      1 + iterate(Translation.Down, coordinate, 0) + iterate(Translation.Up, coordinate, 0)
    ).exists(_ >= board.config.toWin)

    if victory then
      board.copy(status = BoardStatus.Won(player))
    else if board.moves === (board.config.size * board.config.size) then
      board.copy(status = BoardStatus.Drawn)
    else
      board.copy(status = BoardStatus.Ongoing(player.alternate))
  }



