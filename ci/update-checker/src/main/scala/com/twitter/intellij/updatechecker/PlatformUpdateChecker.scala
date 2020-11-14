package com.twitter.intellij.updatechecker

import org.jsoup.Jsoup
import org.virtuslab.ideprobe.dependencies.IntelliJVersion

import scala.jdk.CollectionConverters._

object PlatformUpdateChecker {
  
  private val snapshotsPage = "https://www.jetbrains.com/intellij-repository/snapshots"

  // See https://jsoup.org/cookbook/extracting-data/selector-syntax for explanation
  private val latestIdeaSnapshotSelector = "h2:containsOwn(com.jetbrains.intellij.idea) + table > tbody > tr:eq(1) > td:lt(3)"

  def latestUpdateVersion(): IntelliJVersion = {
    println()
    println(s"Looking for the latest IntelliJ platform snapshots at $snapshotsPage")
    val doc = Jsoup.connect(snapshotsPage).get()

    val latestSnapshotRows = doc.select(latestIdeaSnapshotSelector).asScala

    val buildNumber = latestSnapshotRows(2).text
    IntelliJVersion(buildNumber, None)
  }

}
