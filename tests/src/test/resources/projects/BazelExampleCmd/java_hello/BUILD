java_binary(
    name = "java_hello",
    srcs = glob(["src/main/java/com/hello/*.java"]),
    main_class = "com.hello.Hello",
)

java_test(
    name = "test",
    size = "small",
    srcs = glob([
        "src/test/java/com/hello/tests/*.java",
    ]),
    test_class = "com.hello.tests.HelloTest",
)

load("//ij-seed:ij-seed-rules.bzl", "idea_launcher")
load("//ij-seed:ij-seed-rules.bzl", "idea_seed")

idea_launcher(
    name = "prepare-idea-launcher",
)

idea_seed(
    name = "prepare-idea-seed",
)

sh_binary(
    name = "launch-idea",
    srcs = ["prepare-idea-launcher"],
    data = ["prepare-idea-seed"],
)

#bazel run java_hello//launch-idea
