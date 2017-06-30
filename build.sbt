name := """imdb"""
organization := "br.unb.cic"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.11"

libraryDependencies += filters
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test
libraryDependencies += evolutions
libraryDependencies += jdbc
libraryDependencies += "com.typesafe.play" %% "anorm" % "2.5.1"
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.27"
libraryDependencies += "org.webjars" % "bootstrap" % "3.3.7"
libraryDependencies += "org.webjars.bower" % "jquery" % "3.2.1"
libraryDependencies += "org.webjars" % "font-awesome" % "4.7.0"



// Adds additional packages into Twirl
//TwirlKeys.templateImports += "br.unb.cic.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "br.unb.cic.binders._"
