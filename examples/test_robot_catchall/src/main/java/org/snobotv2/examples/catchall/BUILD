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
    main_class = "org.snobotv2.examples.catchall.Main",
    raw_jni_deps = [
        "@local_rev//:jni_deps",
        "@local_ctre//:jni_deps",
    ],
    visibility = ["//examples/test_robot_catchall:__subpackages__"],
    deps = [
        ":catchall",
        "@local_allwpilib//:wpilibj-java",
    ],
)
