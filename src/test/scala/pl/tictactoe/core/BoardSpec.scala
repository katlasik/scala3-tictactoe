package pl.tictactoe.core

import cats.syntax.all._

class BoardSpec extends munit.FunSuite {

  test("should correctly create board") {
    //when
    val Right(board) = Board.create(5, 3)

    //then
    assert(clue(board.status) == BoardStatus.Ongoing(Player.X))
    assert(clue(board.config.size) == 5)
    assert(clue(board.config.toWin) == 3)
  }

  test("should reject invalid params while creating board") {
    //when
    val result = Board.create(-1, 0)

    //then
    assert(
      clue(result) == Left(
        BoardError.InvalidConfiguration(
          "Fields boardSize must be of size between 3 and 26 and toWin must be equal or smaller than boardSize. Received boardSize: -1, toWin: 0."
        )
      )
    )
  }

  test("should reject moves into same place") {
    //given
    val Right(board) = Board.create(5, 3)
    val Right(updated) = board.move(Coordinate(1, 1), Player.X)

    //when
    val Left(error) = updated.move(Coordinate(1, 1), Player.O)

    //then
    assert(clue(error) == BoardError.FieldAlreadyTaken(Coordinate(1, 1), Player.O))
  }


  test("should reject moves from the wrong player") {
    //given
    val Right(board) = Board.create(5, 3)

    //when
    val Left(error) = board.move(Coordinate(1, 1), Player.O)

    //then
    assert(clue(error) == BoardError.WrongPlayer(Coordinate(1, 1), Player.O))
  }

  test("should reject moves with coordinates out of bounds") {
    //given
    val Right(board) = Board.create(5, 3)

    //when
    val Left(error) = board.move(Coordinate(5, 4), Player.X)

    //then
    assert(clue(error) == BoardError.CoordinateOutOfBound(Coordinate(5, 4)))
  }

  test("should allow playing whole game and winning by one player") {
    //given
    val Right(board) = Board.create(5, 3)

    //when
    val cases = List(
      board.moveMultiple(
        (Coordinate(1, 1), Player.X),
        (Coordinate(1, 2), Player.O),
        (Coordinate(3, 1), Player.X),
        (Coordinate(4, 4), Player.O),
        (Coordinate(1, 4), Player.X),
        (Coordinate(3, 3), Player.O),
        (Coordinate(2, 1), Player.X),
      ) ->
        Player.X,
      board.moveMultiple(
        (Coordinate(4, 1), Player.X),
        (Coordinate(0, 0), Player.O),
        (Coordinate(3, 1), Player.X),
        (Coordinate(1, 1), Player.O),
        (Coordinate(1, 4), Player.X),
        (Coordinate(2, 2), Player.O)
      ) ->
        Player.O,
      board.moveMultiple(
        (Coordinate(3, 3), Player.X),
        (Coordinate(0, 0), Player.O),
        (Coordinate(2, 4), Player.X),
        (Coordinate(1, 1), Player.O),
        (Coordinate(4, 2), Player.X)
      ) ->
        Player.X,
      board.moveMultiple(
        (Coordinate(3, 3), Player.X),
        (Coordinate(0, 0), Player.O),
        (Coordinate(4, 4), Player.X),
        (Coordinate(0, 1), Player.O),
        (Coordinate(4, 2), Player.X),
        (Coordinate(0, 2), Player.O),
      ) -> Player.O
    )

    //then
    cases.foreach {
      case (game, winner) =>

        val Right(Board(_, _, status, _)) = game

        assert(clue(status) == BoardStatus.Won(winner))
    }

  }

  test("should allow playing whole game and drawing") {
    //given
    val Right(board) = Board.create(3, 3)

    //when
    val Right(result) = board.moveMultiple(
        (Coordinate(0, 0), Player.X),
        (Coordinate(0, 1), Player.O),
        (Coordinate(0, 2), Player.X),
        (Coordinate(1, 0), Player.O),
        (Coordinate(1, 2), Player.X),
        (Coordinate(1, 1), Player.O),
        (Coordinate(2, 1), Player.X),
        (Coordinate(2, 2), Player.O),
        (Coordinate(2, 0), Player.X)
      )  

    //then
    assert(clue(result.status) == BoardStatus.Drawn)
  
  }

  test("should disallow playing game after it's finished") {
    //given
    val Right(board) = Board.create(5, 3).flatMap(
      _.moveMultiple(
        (Coordinate(1, 1), Player.X),
        (Coordinate(1, 2), Player.O),
        (Coordinate(3, 1), Player.X),
        (Coordinate(4, 4), Player.O),
        (Coordinate(1, 4), Player.X),
        (Coordinate(3, 3), Player.O),
        (Coordinate(2, 1), Player.X),
      )
    )

    //when
    val result = board.move(Coordinate(4, 4), Player.O)

    //then
    assert(clue(result) == Left(BoardError.GameIsOver(Player.X.some)))
  }

}