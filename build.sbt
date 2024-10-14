lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  //.enablePlugins(PlayNettyServer).disablePlugins(PlayPekkoHttpServer) // Uncomment to use the Netty backend
  .settings(
    name := "play-scala-websocket-example",
    version := "1.0-SNAPSHOT",
    crossScalaVersions := Seq("2.13.14", "3.3.3"),
    scalaVersion := crossScalaVersions.value.head,
    libraryDependencies ++= Seq(
      guice,
      ws,
      "com.typesafe.akka" %% "akka-actor" % "2.6.1",
      "com.typesafe.akka" %% "akka-stream" % "2.6.1",
      "org.webjars" %% "webjars-play" % "3.0.1",
      "org.webjars" % "flot" % "0.8.3-1",
      "org.webjars" % "bootstrap" % "3.3.7-1",
      "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test,
      "org.awaitility" % "awaitility" % "4.2.2" % Test
    ),
    libraryDependencies += "org.apache.commons" % "commons-csv" % "1.9.0",
    
    TwirlKeys.templateImports ++= Seq(
      "views.html.helper.CSPNonce"
    ),
    LessKeys.compress := true,
    (Test / javaOptions) += "-Dtestserver.port=19001",
    scalacOptions ++= Seq(
      "-feature",
      "-Werror"
    )
  )