load("//build_scripts/bazel:java_rules.bzl", "snobot_sim_java_test")

snobot_sim_java_test(
    name = "tests",
    srcs = glob(["*.java"]),
    deps = [
        "//examples/test_robot_wpi/src/main/java/org/snobotv2/examples/wpi/subsystems",
        "//snobot_sim_java_test_utils/src/main/java/org/snobotv2/test_utils",
    ],
)
