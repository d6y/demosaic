version := "1.0.0"

name := "demosaic"

resolvers += "Sonatype Public" at "https://oss.sonatype.org/content/groups/public/"

scalaVersion := "2.11.6"

libraryDependencies ++= scalaz ++ scodec ++ imaging

lazy val imaging = Seq(
  "org.imgscalr"        % "imgscalr-lib" % "4.2",
  "org.apache.sanselan" % "sanselan" % "0.97-incubator")

lazy val scalaz = Seq(
  "org.scalaz" %% "scalaz-core" % "7.1.2"
)

lazy val scodec = Seq(
  "org.scodec" %% "scodec-bits" % "1.0.6",
  "org.scodec" %% "scodec-core" % "1.7.0"
)
