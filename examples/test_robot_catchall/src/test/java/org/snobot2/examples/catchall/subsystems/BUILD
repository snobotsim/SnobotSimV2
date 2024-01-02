load("//build_scripts/bazel:java_rules.bzl", "snobot_sim_java_test")

snobot_sim_java_test(
    name = "test",
    srcs = glob(["*.java"]),
    deps = [
        "//examples/test_robot_catchall/src/main/java/org/snobotv2/examples/catchall/subsystems",
        "//snobot_sim_java_base/src/main/java/org/snobotv2",
    ],
)
