import sbt._
import maven._

class LogulaProject(info: ProjectInfo) extends DefaultProject(info)
                                        with IdeaProject
                                        with MavenDependencies {
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

  /**
   * Test Dependencies
   */
  val simplespec = "com.codahale" %% "simplespec" % "0.3.1" % "test"
  def specs2Framework = new TestFramework("org.specs2.runner.SpecsFramework")
  override def testFrameworks = super.testFrameworks ++ Seq(specs2Framework)
}
