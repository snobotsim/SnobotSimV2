load("//build_scripts/bazel:java_rules.bzl", "snobot_sim_java_library", "snobot_sim_java_robot")

snobot_sim_java_library(
    name = "rev",
    srcs = glob(
        ["*.java"],
        exclude = ["Main.java"],
    ),
    visibility = [
        "//examples/test_robot_rev:__subpackages__",
        "//snobot_sim_java/src/test:__subpackages__",
    ],
    deps = [
        "//examples/test_robot_base/src/main/java/org/snobotv2/examples/base",
        "//examples/test_robot_base/src/main/java/org/snobotv2/examples/base/auton_modes",
        "//examples/test_robot_base/src/main/java/org/snobotv2/examples/base/subsystems",
        "//examples/test_robot_rev/src/main/java/org/snobotv2/examples/rev/subsystems",
        "@local_allwpilib//:cameraserver-java",
        "@local_allwpilib//:cscore-java",
        "@local_allwpilib//:ntcore-java",
        "@local_allwpilib//:wpilibNewCommands-java",
        "@local_allwpilib//:wpilibj-java",
        "@local_allwpilib//:wpiutil-java",
    ],
)

snobot_sim_java_robot(
    name = "Main",
    srcs = ["Main.java"],
    main_class = "org.snobotv2.examples.rev.Main",
    raw_jni_deps = [
        "@local_rev//:jni_deps",
    ],
    visibility = ["//examples/test_robot_rev:__subpackages__"],
    deps = [
        ":rev",
        "@local_allwpilib//:wpilibj-java",
    ],
)
