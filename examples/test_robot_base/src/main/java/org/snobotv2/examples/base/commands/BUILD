load("//build_scripts/bazel:java_rules.bzl", "snobot_sim_java_library")

snobot_sim_java_library(
    name = "commands",
    srcs = glob(["*.java"]),
    visibility = ["//visibility:public"],
    deps = [
        "//examples/test_robot_base/src/main/java/org/snobotv2/examples/base/subsystems",
        "@bazelrio//libraries/java/wpilib/new_commands",
        "@bazelrio//libraries/java/wpilib/wpilibj",
    ],
)
