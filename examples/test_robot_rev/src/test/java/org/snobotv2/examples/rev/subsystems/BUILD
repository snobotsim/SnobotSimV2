load("//build_scripts/bazel:java_rules.bzl", "snobot_sim_java_test")

snobot_sim_java_test(
    name = "tests",
    srcs = glob(["*.java"]),
    deps = [
        "//examples/test_robot_rev/src/main/java/org/snobotv2/examples/rev/subsystems",
        "//snobot_sim_java_test_utils/src/main/java/org/snobotv2/test_utils",
        "@bazelrio//libraries/java/wpilib/hal",
        "@bazelrio//libraries/java/wpilib/wpilibj",
        "@bazelrio//libraries/java/wpilib/wpiutil",
    ],
)
