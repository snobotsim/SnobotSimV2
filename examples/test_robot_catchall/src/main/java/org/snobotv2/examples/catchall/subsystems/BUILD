load("//build_scripts/bazel:java_rules.bzl", "snobot_sim_java_library")

snobot_sim_java_library(
    name = "subsystems",
    srcs = glob(["*.java"]),
    visibility = ["//examples/test_robot_catchall:__subpackages__"],
    deps = [
        "@local_allwpilib//:wpilibNewCommands-java",
        "@local_allwpilib//:wpilibj-java",
        "@local_ctre//:ctre-java",
        "@local_navx//:navx-java",
        "@local_rev//:rev-java",
    ],
)
