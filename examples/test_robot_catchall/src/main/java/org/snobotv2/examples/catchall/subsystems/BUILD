load("//build_scripts/bazel:java_rules.bzl", "snobot_sim_java_library")

snobot_sim_java_library(
    name = "subsystems",
    srcs = glob(["*.java"]),
    visibility = ["//examples/test_robot_catchall:__subpackages__"],
    deps = [
        "@bazelrio//libraries/java/ctre/phoenix",
        "@bazelrio//libraries/java/kauailabs/navx",
        "@bazelrio//libraries/java/rev/revlib",
        "@bazelrio//libraries/java/wpilib/new_commands",
        "@bazelrio//libraries/java/wpilib/wpilibj",
    ],
)
