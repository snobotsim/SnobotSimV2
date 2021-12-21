load("//build_scripts/bazel:java_rules.bzl", "snobot_sim_java_library", "snobot_sim_java_robot")

snobot_sim_java_library(
    name = "ctre",
    srcs = glob(
        ["*.java"],
        exclude = ["Main.java"],
    ),
    visibility = [
        "//examples/test_robot_ctre:__subpackages__",
        "//snobot_sim_java/src/test:__subpackages__",
    ],
    deps = [
        "//examples/test_robot_base/src/main/java/org/snobotv2/examples/base",
        "//examples/test_robot_base/src/main/java/org/snobotv2/examples/base/auton_modes",
        "//examples/test_robot_base/src/main/java/org/snobotv2/examples/base/subsystems",
        "//examples/test_robot_ctre/src/main/java/org/snobotv2/examples/ctre/subsystems",
        "@bazelrio//libraries/java/ctre/phoenix",
        "@bazelrio//libraries/java/wpilib/cameraserver",
        "@bazelrio//libraries/java/wpilib/cscore",
        "@bazelrio//libraries/java/wpilib/new_commands",
        "@bazelrio//libraries/java/wpilib/ntcore",
        "@bazelrio//libraries/java/wpilib/wpilibj",
        "@bazelrio//libraries/java/wpilib/wpiutil",
    ],
)

snobot_sim_java_robot(
    name = "Main",
    srcs = ["Main.java"],
    main_class = "org.snobotv2.examples.ctre.Main",
    visibility = ["//examples/test_robot_ctre:__subpackages__"],
    deps = [
        ":ctre",
        "@bazelrio//libraries/java/wpilib/wpilibj",
    ],
)
