load("//build_scripts/bazel:java_rules.bzl", "snobot_sim_java_library")

snobot_sim_java_library(
    name = "subsystems",
    srcs = glob(["*.java"]),
    visibility = ["//visibility:public"],
    deps = [
        "@local_allwpilib//:wpilibNewCommands-java",
        "@local_allwpilib//:wpilibj-java",
        "@local_allwpilib//:wpimath-java",
    ],
)
