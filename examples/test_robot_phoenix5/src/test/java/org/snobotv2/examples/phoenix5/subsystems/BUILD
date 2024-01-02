load("//build_scripts/bazel:java_rules.bzl", "snobot_sim_java_test")

snobot_sim_java_test(
    name = "tests",
    srcs = glob(["*.java"]),
    deps = [
        "//examples/test_robot_base/src/main/java/org/snobotv2/examples/base/subsystems",
        "//examples/test_robot_phoenix5/src/main/java/org/snobotv2/examples/phoenix5/subsystems",
        "//snobot_sim_java_test_utils/src/main/java/org/snobotv2/test_utils",
        "@bzlmodrio-allwpilib//libraries/java/wpimath",
    ],
)
