package org.virtuslab.tests.pants

import java.util.concurrent.{ExecutorService, Executors, ForkJoinPool, SynchronousQueue, ThreadPoolExecutor, TimeUnit}
import org.virtuslab.ideprobe.{Assertions, IdeProbeFixture, Shell}
import org.virtuslab.ideprobe.robot.RobotPluginExtension
import scala.concurrent.{ExecutionContext, Future}

object BUILDFilesTest extends IdeProbeFixture
  with RobotPluginExtension with Assertions {

  val pool = new ThreadPoolExecutor(0, Integer
    .MAX_VALUE, 5L, TimeUnit.SECONDS, new SynchronousQueue[Runnable])
  pool.allowCoreThreadTimeOut(true)
  override protected implicit val ec: ExecutionContext = ExecutionContext.fromExecutor(pool)

  def main(args: Array[String]): Unit = {
    fixtureFromConfig().run { intelliJ =>
      intelliJ.probe.withRobot.openProject(intelliJ.workspace)
      intelliJ.probe.projectModel()
    }
    pool.shutdownNow()
  }

}
