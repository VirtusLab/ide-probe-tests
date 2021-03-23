java_binary(
    name = "java_hello",
    srcs = glob(["src/main/java/com/hello/*.java"]),
    main_class = "com.hello.Hello"
)

java_test(
    name = "test",
    size = "small",
    srcs = glob([
        "src/test/java/com/hello/tests/*.java"
    ]),
    deps = [
        "//thrift_hello:thrift_hello_scrooge"
    ],
    test_class = "com.hello.tests.HelloTest",
)
