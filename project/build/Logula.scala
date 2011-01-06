import sbt._

class Logula(info: ProjectInfo) extends DefaultProject(info) with IdeaProject with posterous.Publish with maven.MavenDependencies {
  /**
   * Publish the source as well as the class files.
   */
  override def packageSrcJar= defaultJarPath("-sources.jar")
  val sourceArtifact = sbt.Artifact(artifactID, "src", "jar", Some("sources"), Nil, None)
  override def packageToPublishActions = super.packageToPublishActions ++ Seq(packageSrc)

  override def compileOptions = super.compileOptions ++
    Seq(Deprecation, ExplainTypes, Unchecked)
  
  lazy val publishTo = Resolver.sftp("repo.codahale.com",
                                     "codahale.com",
                                     "/home/codahale/repo.codahale.com/")

  val codaRepo = "Coda's Repo" at "http://repo.codahale.com"

  /**
   * Dependencies
   */
  val log4j = "log4j" % "log4j" % "1.2.16"
  
  val specs = "org.scala-tools.testing" %% "specs" % "1.6.6" % "test"
  val simplespec = "com.codahale" %% "simplespec" % "0.2.0" % "test"
  val mockito = "org.mockito" % "mockito-all" % "1.8.4" % "test"
}
