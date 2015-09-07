

enablePlugins(ScalaJSPlugin)

name := "macros"

version := "0.5"

organization := "com.github.chandu0101"

scalaVersion := "2.11.7"

val scalatestVersion = "3.0.0-M6"

libraryDependencies +=   "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided

relativeSourceMaps := true

scalacOptions += "-deprecation"

scalacOptions += "-feature"

//======================== Publication Settings =========================\\

homepage := Some(url("https://github.com/chandu0101/macros"))
licenses  += ("Apache-2.0", url("http://opensource.org/licenses/Apache-2.0"))

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

pomExtra :=
  <scm>
    <connection>scm:git:github.com:chandu0101/macros</connection>
    <developerConnection>scm:git:git@github.com:chandu0101/macros.git</developerConnection>
    <url>github.com:chandu0101/macros.git</url>
  </scm>
    <developers>
      <developer>
        <id>chandu0101</id>
        <name>Chandra Sekhar Kode</name>
      </developer>
    </developers>


//================ Testing settings =====================\\
libraryDependencies += "org.scalatest" %%% "scalatest" % scalatestVersion % Test

scalaJSStage in Global := FastOptStage

jsDependencies += RuntimeDOM

jsEnv in Test := new PhantomJS2Env(scalaJSPhantomJSClassLoader.value, addArgs = Seq("--web-security=no"))
