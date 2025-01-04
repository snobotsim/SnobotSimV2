load("//build_scripts/bazel:java_rules.bzl", "snobot_sim_java_library")

snobot_sim_java_library(
    name = "subsystems",
    srcs = glob(["*.java"]),
    visibility = ["//examples/test_robot_catchall:__subpackages__"],
    deps = [
        "@bzlmodrio-allwpilib//libraries/java/wpilibNewCommands",
        "@bzlmodrio-allwpilib//libraries/java/wpilibj",
        "@bzlmodrio-studica//libraries/java/studica-java",
        "@bzlmodrio-phoenix//libraries/java/wpiapi-java",
        "@bzlmodrio-revlib//libraries/java/revlib-java",
    ],
)
