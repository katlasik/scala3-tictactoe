name := "Scala3"

version := "0.1"

scalaVersion := "3.1.0"

scalacOptions ++= Seq(
  "-indent",
  "-new-syntax",
  "-explain"
)

libraryDependencies += "org.typelevel" %% "cats-effect" % "3.3.1"
libraryDependencies += "org.typelevel" %% "cats-core" % "2.7.0"
libraryDependencies += "org.typelevel" %% "munit-cats-effect-3" % "1.0.7" % Test