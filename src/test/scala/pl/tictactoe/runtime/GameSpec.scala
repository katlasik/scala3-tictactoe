package pl.tictactoe.runtime

import cats.effect.IO
import pl.tictactoe.core.BoardConfig
import pl.tictactoe.core.Board
import pl.tictactoe.runtime.Game

class GameSpec extends munit.CatsEffectSuite:

    val VictoryX = List(
        "A1",
        "B1",
        "A2",
        "B2",
        "A3",
    )

    val VictoryO = List(
        "A1",
        "B1",
        "A2",
        "B2",
        "C3",
        "B3"
    )

    val Draw = List(
        "A1",
        "A2",
        "A3",
        "B1",
        "B3",
        "B2",
        "C2",
        "C3",
        "C1"
    )

    test("should prepare board") {

        for
            console <- TestConsole.create("4", "3") 
            board <- Game(console).prepareBoard
        yield
            assert(clue(board.config) == BoardConfig(4, 3))

    }

    test("should reject invalid input while creating game") {

        for
            console <- TestConsole.create("bad", "5", "3") 
            board <- Game(console).prepareBoard
            lines <- console.printedLines
        yield
            assert(clue(board.config) == BoardConfig(5, 3))
            assert(clue(lines.contains("Invalid value! Please enter value between 3 and 26.")))
    }

    test("should allow playing whole game by player X") {

        for
            console <- TestConsole.create(VictoryX*) 
            board <- IO.fromEither(Board.create(3, 3))
            _ <- Game(console).loop(board)
            lines <- console.printedLines
        yield
            assert(clue(lines.contains("\nPlayer X won the game!\n")))
    }

     test("should allow playing whole game by player O") {

        for
            console <- TestConsole.create(VictoryO*) 
            board <- IO.fromEither(Board.create(3, 3))
            _ <- Game(console).loop(board)
            lines <- console.printedLines
        yield
            assert(clue(lines.contains("\nPlayer O won the game!\n")))
    }

    test("should allow playing whole game with draw") {

        for
            console <- TestConsole.create(Draw*) 
            board <- IO.fromEither(Board.create(3, 3))
            _ <- Game(console).loop(board)
            lines <- console.printedLines
        yield
            assert(clue(lines.contains("\nDraw!\n")))
    }

    test("should reject invalid input while playing") {

        for
            console <- TestConsole.create("bad" +: Draw*) 
            board <- IO.fromEither(Board.create(3, 3))
            _ <- Game(console).loop(board)
            lines <- console.printedLines
        yield
            assert(clue(lines.contains("Please enter correct coordinate:")))
    }
    