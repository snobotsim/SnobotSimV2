load("//build_scripts/bazel:java_rules.bzl", "snobot_sim_java_library", "snobot_sim_java_robot")

snobot_sim_java_library(
    name = "rev",
    srcs = glob(["*.java"]),
    visibility = [
        "//examples/test_robot_rev:__subpackages__",
        "//snobot_sim_java_base/src/test:__subpackages__",
        "//snobot_sim_java_revlib/src/test:__subpackages__",
    ],
    deps = [
        "//examples/test_robot_base/src/main/java/org/snobotv2/examples/base",
        "//examples/test_robot_base/src/main/java/org/snobotv2/examples/base/auton_modes",
        "//examples/test_robot_base/src/main/java/org/snobotv2/examples/base/subsystems",
        "//examples/test_robot_rev/src/main/java/org/snobotv2/examples/rev/subsystems",
        "@bzlmodrio-allwpilib//libraries/java/cameraserver",
        "@bzlmodrio-allwpilib//libraries/java/wpilibNewCommands",
        "@bzlmodrio-allwpilib//libraries/java/wpilibj",
    ],
)

snobot_sim_java_robot(
    name = "Main",
    main_class = "org.snobotv2.examples.rev.Main",
    visibility = ["//examples/test_robot_rev:__subpackages__"],
    runtime_deps = [":rev"],
)
