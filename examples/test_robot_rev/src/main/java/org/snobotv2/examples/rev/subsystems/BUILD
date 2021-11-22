load("//build_scripts/bazel:java_rules.bzl", "snobot_sim_java_library")

snobot_sim_java_library(
    name = "subsystems",
    srcs = glob(["*.java"]),
    visibility = [
        "//examples/test_robot_rev:__subpackages__",
        "//snobot_sim_java/src/test:__subpackages__",
    ],
    deps = [
        "//examples/test_robot_base/src/main/java/org/snobotv2/examples/base",
        "//examples/test_robot_base/src/main/java/org/snobotv2/examples/base/subsystems",
        "//snobot_sim_java/src/main/java/com/revrobotics:simmable_rev",
        "//snobot_sim_java/src/main/java/org/snobotv2/module_wrappers/navx",
        "//snobot_sim_java/src/main/java/org/snobotv2/module_wrappers/rev",
        "//snobot_sim_java/src/main/java/org/snobotv2/sim_wrappers",
        "@local_allwpilib//:wpilibNewCommands-java",
        "@local_allwpilib//:wpilibj-java",
        "@local_allwpilib//:wpimath-java",
        "@local_navx//:navx-java",
        "@local_rev//:rev-java",
    ],
)
