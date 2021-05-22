package pl.tictactoe.core

import cats.syntax.all._
import cats.Show
import scala.collection.immutable.ArraySeq

opaque type Fields = ArraySeq[ArraySeq[FieldStatus]]

object Fields:
  given Show[Fields] with
    def show(f: Fields) =
      val numbers = LazyList.from(1).take(f.size).map(String.format("%1$2d", _))
      val letters = LazyList.from('A').take(f.size).map(_.toChar.toString)

      ("   " + letters.mkString(" ") + "\n") + f.zip(numbers).map {
        case (line, number) =>
          (number + " ") + line.map {
            case FieldStatus.Empty => " "
            case FieldStatus.Taken(Player.X) => "X"
            case FieldStatus.Taken(Player.O) => "O"
          }.mkString("|")
      }.mkString("\n   " + "- " * f.size + "\n")

  def create(boardSize: Int): Fields = ArraySeq.fill(boardSize)(ArraySeq.fill(boardSize)(FieldStatus.Empty))

  extension (f: Fields)
    def apply(c: Coordinate): Option[FieldStatus] = f.get(c.y).flatMap(_.get(c.x))

    def update(c: Coordinate, fieldStatus: FieldStatus): Option[Fields] =
      import c.{x, y}
      for
        line <- f.get(y)
        cell <- line.get(x)
      yield f.updated(y, line.updated(x, fieldStatus))