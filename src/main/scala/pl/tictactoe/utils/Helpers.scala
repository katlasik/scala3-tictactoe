package pl.tictactoe.utils
import cats.syntax.all._

object Helpers:

  def parseInt(min: Int, max: Int)(s: String): Option[Int] = Either.catchNonFatal(s.toInt).toOption
    .filter(i => i >= min && i <= max)
