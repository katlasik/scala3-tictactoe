package pl.tictactoe.runtime

import cats.effect.IO
import cats.syntax.all._
import pl.tictactoe.utils.Helpers
import pl.tictactoe.utils.Globals
import pl.tictactoe.core._

class Game(console: Console):
    
  private[runtime] def loop(board: Board): IO[Unit] = for
    _ <- console.printLine(show"\n${board.fields}\n")
    _ <- board.status match
        case BoardStatus.Drawn => console.printLine("\nDraw!\n")
        case BoardStatus.Won(player) => console.printLine(show"\nPlayer ${player} won the game!\n")
        case BoardStatus.Ongoing(nextPlayer) => 
          for
            _ <- console.printLine("")
            move <- readLoop(
              show"Player $nextPlayer please make a move: ",
              "Please enter correct coordinate:",
              CoordinateParser.parse  
            )
            _ <- board.move(move, nextPlayer) match
              case Right(updated) => loop(updated)
              case Left(error) => console.printLine(error.getMessage) *> loop(board)
          yield ()
  yield ()

  private[runtime] val prepareBoard = for
    _ <- console.printLine("\nStarting new game...\n")
    boardSize <- readLoopInt(
      "Please enter the size of the board:",
      s"Invalid value! Please enter value between ${Globals.MinBoardSize} and ${Globals.MaxBoardSize}.",
      Globals.MinBoardSize,
      Globals.MaxBoardSize
    )
    toWin <- readLoopInt(
      "Please enter how long should be line to win:",
      s"Invalid value! Please enter value between ${Globals.MinBoardSize} and $boardSize.",
      Globals.MinBoardSize,
      boardSize
    )
    board <- IO.fromEither(Board.create(boardSize, toWin))
  yield board

  private def readLoopInt(msg: String, errorMsg: String, min: Int, max: Int) = 
    readLoop(msg, errorMsg, Helpers.parseInt(min, max))

  private def readLoop[O](msg: String, errorMsg: String, read: String => Option[O]): IO[O] = for
    _ <- console.printLine(msg)
    line <- console.readLine
    result <- read(line) match
      case Some(result) => result.pure[IO]
      case None => console.printLine(errorMsg) *> readLoop(msg, errorMsg, read)
  yield result


  val run: IO[Unit] = for
    board <- prepareBoard
    _ <- loop(board)
  yield ()