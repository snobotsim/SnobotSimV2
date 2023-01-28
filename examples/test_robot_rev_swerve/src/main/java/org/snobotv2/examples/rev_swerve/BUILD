load("//build_scripts/bazel:java_rules.bzl", "snobot_sim_java_library", "snobot_sim_java_robot")

snobot_sim_java_library(
    name = "rev_swerve",
    srcs = glob(
        [
            "*.java",
        ],
        exclude = ["Main.java"],
    ),
    visibility = [
        "//examples/test_robot_ctre_swerve:__subpackages__",
    ],
    deps = [
        "//examples/test_robot_base_swerve/src/main/java/org/snobotv2/examples/base_swerve/commands",
        "//examples/test_robot_base_swerve/src/main/java/org/snobotv2/examples/base_swerve/commands/auto",
        "//examples/test_robot_base_swerve/src/main/java/org/snobotv2/examples/base_swerve/commands/tuning",
        "//examples/test_robot_base_swerve/src/main/java/org/snobotv2/examples/base_swerve/subsystems",
        "//examples/test_robot_rev_swerve/src/main/java/org/snobotv2/examples/rev_swerve/subsystems",
        "@bzlmodrio-allwpilib//libraries/cpp/wpiutil:jni",
        "@bzlmodrio-allwpilib//libraries/java/cameraserver",
        "@bzlmodrio-allwpilib//libraries/java/wpilibNewCommands",
        "@bzlmodrio-allwpilib//libraries/java/wpilibj",
        "@bzlmodrio-allwpilib//libraries/java/wpimath",
        "@bzlmodrio-allwpilib//libraries/java/wpiutil",
        "@bzlmodrio-phoenix//libraries/java/api-java",
        "@bzlmodrio-phoenix//libraries/java/wpiapi-java",
        "@bzlmodrio-revlib//libraries/java/revlib-java",
    ],
)

snobot_sim_java_robot(
    name = "Main",
    srcs = ["Main.java"],
    main_class = "org.snobotv2.examples.rev_swerve.Main",
    visibility = ["//examples/test_robot_rev_swerve:__subpackages__"],
    deps = [
        ":rev_swerve",
        "@bzlmodrio-allwpilib//libraries/java/wpilibj",
    ],
)
