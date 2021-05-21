package pl.tictactoe.core

class CoordinateParserSpec extends munit.FunSuite {

  test("should parse correct coordinates") {

    //when
    val cases = List(
      ("A1", Some(Coordinate(0, 0))),
      ("1A", Some(Coordinate(0, 0))),
      ("Z22", Some(Coordinate(25, 21))),
      ("22Z", Some(Coordinate(25, 21))),
      ("W2", Some(Coordinate(22, 1))),
      ("2W", Some(Coordinate(22, 1))),
      ("O11", Some(Coordinate(14, 10))),
      ("11O", Some(Coordinate(14, 10))),
      ("A12", Some(Coordinate(0, 11))),
      ("12A", Some(Coordinate(0, 11))),
      ("C 15", Some(Coordinate(2, 14))),
      ("15 C", Some(Coordinate(2, 14)))
    )

    //then
    cases.foreach {
      case (rawString, expected) =>
        assert(clue(CoordinateParser.parse(rawString)) == expected)
    }

  }

  test("should reject incorrect coordinates") {

    //when
    val cases = List(
      "A0",
      "Z29",
      "!!",
      "B-1",
      "BB4",
      "1B2",
      ""
    )

    //then
    cases.foreach(rawString =>
      assert(clue(CoordinateParser.parse(rawString)) == None)
    )

  }

}
