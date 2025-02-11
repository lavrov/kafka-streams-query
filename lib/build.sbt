import Dependencies._

name := "kafka-streams-query"

organization := "com.lightbend"

version := "0.2.0-SNAPSHOT"

scalaVersion := Versions.scalaVersion

crossScalaVersions := Versions.crossScalaVersions

scalacOptions := Seq("-unchecked", "-deprecation")

Test / parallelExecution := false

libraryDependencies ++= Seq(
  kafkaStreams excludeAll(ExclusionRule("org.slf4j", "slf4j-log4j12"), ExclusionRule("org.apache.zookeeper", "zookeeper")),
  scalaLogging,
  circeCore,
  circeGeneric,
  circeParser,
  akkaHttp,
  akkaStreams,
  akkaHttpCirce,
  akkaSlf4j,
  bijection
)

licenses := Seq("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))

developers := List(
  Developer("debasishg", "Debasish Ghosh", "@debasishg", url("https://github.com/debasishg")),
  Developer("blublinsky", "Boris Lublinsky", "@blublinsky", url("https://github.com/blublinsky")),
  Developer("maasg", "Gerard Maas", "@maasg", url("https://github.com/maasg")),
  Developer("seglo", "Sean Glover", "@seglo", url("https://github.com/seglo"))
)

organizationName := "lightbend"

organizationHomepage := Some(url("http://lightbend.com/"))

homepage := scmInfo.value map (_.browseUrl)

scmInfo := Some(ScmInfo(url("https://github.com/lightbend/kafka-streams-query"), "git@github.com:lightbend/kafka-streams-query.git"))

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

Test / publishArtifact := false
