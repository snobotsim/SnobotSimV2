load("//build_scripts/bazel:java_rules.bzl", "snobot_sim_java_library", "snobot_sim_java_robot")

snobot_sim_java_library(
    name = "phoenix5",
    srcs = glob(["*.java"]),
    visibility = [
        "//examples/test_robot_phoenix5:__subpackages__",
        "//snobot_sim_java/src/test:__subpackages__",
    ],
    deps = [
        "//examples/test_robot_base/src/main/java/org/snobotv2/examples/base",
        "//examples/test_robot_base/src/main/java/org/snobotv2/examples/base/auton_modes",
        "//examples/test_robot_base/src/main/java/org/snobotv2/examples/base/subsystems",
        "//examples/test_robot_phoenix5/src/main/java/org/snobotv2/examples/phoenix5/subsystems",
        "@bzlmodrio-allwpilib//libraries/java/cameraserver",
        "@bzlmodrio-allwpilib//libraries/java/wpilibNewCommands",
        "@bzlmodrio-allwpilib//libraries/java/wpilibj",
        "@bzlmodrio-phoenix//libraries/java/api-java",
        "@bzlmodrio-phoenix//libraries/java/wpiapi-java",
    ],
)

snobot_sim_java_robot(
    name = "Main",
    main_class = "org.snobotv2.examples.phoenix5.Main",
    visibility = ["//examples/test_robot_phoenix5:__subpackages__"],
    runtime_deps = [":phoenix5"],
)
