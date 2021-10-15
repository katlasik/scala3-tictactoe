name := "Scala3"

version := "0.1"

scalaVersion := "3.0.2"

scalacOptions ++= Seq(
  "-indent",
  "-new-syntax",
  "-explain"
)

libraryDependencies += "org.typelevel" %% "cats-effect" % "3.2.9"
libraryDependencies += "org.typelevel" %% "cats-core" % "2.6.1"
libraryDependencies += "org.typelevel" %% "munit-cats-effect-3" % "1.0.6" % Test