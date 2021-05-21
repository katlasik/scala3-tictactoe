package pl.tictactoe

import cats.effect.IOApp
import cats.effect.IO

import pl.tictactoe.runtime.LiveConsole
import pl.tictactoe.runtime.Game

object Main extends IOApp.Simple:

  val run = Game(LiveConsole).run.foreverM
  