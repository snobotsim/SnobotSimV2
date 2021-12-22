load("//build_scripts/bazel:java_rules.bzl", "snobot_sim_java_library", "snobot_sim_java_robot")

snobot_sim_java_library(
    name = "catchall",
    srcs = glob(
        ["*.java"],
        exclude = ["Main.java"],
    ),
    visibility = ["//examples/test_robot_catchall:__subpackages__"],
    deps = [
        "//examples/test_robot_catchall/src/main/java/org/snobotv2/examples/catchall/subsystems",
        "@bazelrio//libraries/java/wpilib/cameraserver",
        "@bazelrio//libraries/java/wpilib/new_commands",
        "@bazelrio//libraries/java/wpilib/wpilibj",
    ],
)

snobot_sim_java_robot(
    name = "Main",
    srcs = ["Main.java"],
    main_class = "org.snobotv2.examples.catchall.Main",
    visibility = ["//examples/test_robot_catchall:__subpackages__"],
    deps = [
        ":catchall",
        "@bazelrio//libraries/java/wpilib/wpilibj",
    ],
)
