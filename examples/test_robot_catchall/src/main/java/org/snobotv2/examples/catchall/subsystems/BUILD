load("//build_scripts/bazel:java_rules.bzl", "snobot_sim_java_library")

snobot_sim_java_library(
    name = "subsystems",
    srcs = glob(["*.java"]),
    visibility = ["//examples/test_robot_catchall:__subpackages__"],
    deps = [
        "@bzlmodrio-allwpilib//libraries/java/commandsv2",
        "@bzlmodrio-allwpilib//libraries/java/drivers",
        "@bzlmodrio-allwpilib//libraries/java/wpilibj",
        "@bzlmodrio-revlib//libraries/java/revlib-java",
    ],
)
