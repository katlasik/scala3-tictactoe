package pl.tictactoe.core

enum BoardStatus:
  case Ongoing(nextPlayer: Player)
  case Won(winner: Player)
  case Drawn
