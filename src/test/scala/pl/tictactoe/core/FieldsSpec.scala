package pl.tictactoe.core

import cats.syntax.all._

class FieldsSpec extends munit.FunSuite:

    test("should correctly show fields") {

        val fields = Fields.create(3)

        val Some(updated) = fields.update(Coordinate(0,0), FieldStatus.Taken(Player.X))
            .flatMap(_.update(Coordinate(1,0), FieldStatus.Taken(Player.X)))
            .flatMap(_.update(Coordinate(2,0), FieldStatus.Taken(Player.X)))
            .flatMap(_.update(Coordinate(0,1), FieldStatus.Taken(Player.O)))
            .flatMap(_.update(Coordinate(1,1), FieldStatus.Taken(Player.O)))
            .flatMap(_.update(Coordinate(2,1), FieldStatus.Taken(Player.O)))

        val expected = """|   A B C
                          | 1 X|X|X
                          |   - - - 
                          | 2 O|O|O
                          |   - - - 
                          | 3  | | """.stripMargin

        assert(clue(updated.show) == expected)

    }