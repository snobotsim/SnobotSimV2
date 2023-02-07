load("//build_scripts/bazel:java_rules.bzl", "snobot_sim_java_library", "snobot_sim_java_robot")

snobot_sim_java_library(
    name = "catchall",
    srcs = glob(["*.java"]),
    visibility = ["//examples/test_robot_catchall:__subpackages__"],
    deps = [
        "//examples/test_robot_catchall/src/main/java/org/snobotv2/examples/catchall/subsystems",
        "@bzlmodrio-allwpilib//libraries/java/cameraserver",
        "@bzlmodrio-allwpilib//libraries/java/wpilibNewCommands",
        "@bzlmodrio-allwpilib//libraries/java/wpilibj",
    ],
)

snobot_sim_java_robot(
    name = "Main",
    main_class = "org.snobotv2.examples.catchall.Main",
    visibility = ["//examples/test_robot_catchall:__subpackages__"],
    runtime_deps = [":catchall"],
)
