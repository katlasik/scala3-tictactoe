package pl.tictactoe.core

import cats.Show
import cats.Eq

enum Player:
  def alternate: Player = this match
    case X => O
    case O => X
    
  case X
  case O

object Player:
  given Show[Player] = Show.fromToString
  given Eq[Player] = Eq.fromUniversalEquals