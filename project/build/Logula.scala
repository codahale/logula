class Logula(info: sbt.ProjectInfo) extends sbt.DefaultProject(info) with posterous.Publish with rsync.RsyncPublishing {
  /**
   * Publish the source as well as the class files.
   */
  override def packageSrcJar= defaultJarPath("-sources.jar")
  val sourceArtifact = sbt.Artifact(artifactID, "src", "jar", Some("sources"), Nil, None)
  override def packageToPublishActions = super.packageToPublishActions ++ Seq(packageSrc)

  override def compileOptions = super.compileOptions ++
    Seq(Deprecation, ExplainTypes, Unchecked)

  /**
   * Publish via rsync.
   */
  def rsyncRepo = "codahale.com:/home/codahale/repo.codahale.com"

  /**
   * Dependencies
   */
  val log4j = "log4j" % "log4j" % "1.2.16" withSources() intransitive()
  val log4jExtras = "log4j" % "apache-log4j-extras" % "1.0" withSources() intransitive()

  val specs = "org.scala-tools.testing" %% "specs" % "1.6.5" % "test" withSources ()
  val simplespec = "com.codahale" %% "simplespec" % "0.2.0-SNAPSHOT" % "test" withSources ()
  val mockito = "org.mockito" % "mockito-all" % "1.8.4" % "test" withSources ()
}
