package com.virtuslab.bazel.protocol

import org.virtuslab.ideprobe.jsonrpc.JsonRpc.Method.Request
import org.virtuslab.ideprobe.jsonrpc.PayloadJsonFormat._

object BazelEndpoints {
  val SetupBazelExecutable = Request[String, Unit]("bazel/settings/executable")
}
