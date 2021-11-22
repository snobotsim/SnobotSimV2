load("//build_scripts/bazel:java_rules.bzl", "snobot_sim_java_library")

snobot_sim_java_library(
    name = "commands",
    srcs = glob(["*.java"]),
    visibility = ["//visibility:public"],
    deps = [
        "//examples/test_robot_base/src/main/java/org/snobotv2/examples/base/subsystems",
        "@local_allwpilib//:wpilibNewCommands-java",
        "@local_allwpilib//:wpilibj-java",
    ],
)
