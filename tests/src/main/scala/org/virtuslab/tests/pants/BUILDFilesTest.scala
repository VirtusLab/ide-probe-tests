package org.virtuslab.tests.pants

import java.util.concurrent.{ExecutorService, Executors, ForkJoinPool, SynchronousQueue, ThreadPoolExecutor, TimeUnit}
import org.virtuslab.ideprobe.{Assertions, IdeProbeFixture, Shell}
import org.virtuslab.ideprobe.robot.RobotPluginExtension
import scala.concurrent.{ExecutionContext, Future}

object BUILDFilesTest extends IdeProbeFixture
  with RobotPluginExtension with Assertions {

  private val pool: ExecutorService = Executors.newCachedThreadPool
  override protected implicit val ec: ExecutionContext = ExecutionContext.fromExecutor(pool)

  def main(args: Array[String]): Unit = {
    fixtureFromConfig().run { intelliJ =>
      intelliJ.probe.withRobot.openProject(intelliJ.workspace)
      intelliJ.probe.projectModel()
    }
    pool.shutdown()
    pool.shutdownNow()
    println("Pools killed")
    Shell.run("jstack", ProcessHandle.current().pid().toString)
  }

}
