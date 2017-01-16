lazy val root = (project in file(".")).
  settings(
    name := "Lotto",
    version := "1.0",
    scalaVersion :=  "2.11.8" // "2.12.1"
  )
resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"  //scalatest
  
libraryDependencies ++= Seq(
	"io.spray" %%  "spray-json" % "1.3.3"
	,"org.scalactic" %% "scalactic" % "3.0.1"  % "test"
	,"org.scalatest" %% "scalatest" % "3.0.1" % "test"
	,"org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
	)

scalacOptions ++= Seq("-feature", "-deprecation")
scalacOptions in Test ++= Seq("-Yrangepos")
testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-l", "org.scalatest.tags.Slow")

EclipseKeys.withSource := true
EclipseKeys.withJavadoc := true