name := "backend"

version := "1.0"

lazy val `backend` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"

scalaVersion := "2.13.3"

libraryDependencies ++= Seq(jdbc, ehcache, ws, specs2 % Test, guice)

libraryDependencies ++= Seq(
  "org.postgresql" % "postgresql" % "42.2.16",
  "org.mindrot" % "jbcrypt" % "0.3m"
)

libraryDependencies += "io.getquill" %% "quill-jdbc" % "3.6.0"

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

      