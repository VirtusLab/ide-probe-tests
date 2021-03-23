load("@io_bazel_rules_scala//thrift:thrift.bzl", "thrift_library")
load("@io_bazel_rules_scala//scala:scala.bzl", "scala_binary", "scala_library")
load("@io_bazel_rules_scala//twitter_scrooge:twitter_scrooge.bzl", "scrooge_scala_library")
load("@io_bazel_rules_scala//twitter_scrooge/toolchain:toolchain.bzl", "export_scrooge_deps", "scrooge_toolchain")

thrift_library(
    name = "thrift_hello",
    srcs = ["src/hello.thrift"],
    visibility = ["//visibility:public"]
)

scrooge_scala_library(
    name = "thrift_hello_scrooge",
    visibility = ["//visibility:public"],
    exports = [],
    deps = ["//thrift_hello"]
)
