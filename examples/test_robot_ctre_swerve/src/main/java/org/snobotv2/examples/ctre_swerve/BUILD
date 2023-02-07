load("//build_scripts/bazel:java_rules.bzl", "snobot_sim_java_library", "snobot_sim_java_robot")

snobot_sim_java_library(
    name = "ctre_swerve",
    srcs = glob(["**/*.java"]),
    visibility = [
        "//examples/test_robot_ctre_swerve:__subpackages__",
    ],
    deps = [
        "//examples/test_robot_base_swerve/src/main/java/org/snobotv2/examples/base_swerve",
        "//examples/test_robot_base_swerve/src/main/java/org/snobotv2/examples/base_swerve/commands",
        "//examples/test_robot_base_swerve/src/main/java/org/snobotv2/examples/base_swerve/commands/auto",
        "//examples/test_robot_base_swerve/src/main/java/org/snobotv2/examples/base_swerve/commands/tuning",
        "//examples/test_robot_base_swerve/src/main/java/org/snobotv2/examples/base_swerve/subsystems",
        "//snobot_sim_java/src/main/java/org/snobotv2/module_wrappers/ctre",
        "//snobot_sim_java/src/main/java/org/snobotv2/sim_wrappers",
        "//snobot_swerve_sim/src/main/java/edu/wpi/first/wpilibj/simulation/swerve",
        "@bzlmodrio-allwpilib//libraries/cpp/wpiutil:jni",
        "@bzlmodrio-allwpilib//libraries/java/cameraserver",
        "@bzlmodrio-allwpilib//libraries/java/wpilibNewCommands",
        "@bzlmodrio-allwpilib//libraries/java/wpilibj",
        "@bzlmodrio-allwpilib//libraries/java/wpimath",
        "@bzlmodrio-allwpilib//libraries/java/wpiutil",
        "@bzlmodrio-phoenix//libraries/java/api-java",
        "@bzlmodrio-phoenix//libraries/java/wpiapi-java",
    ],
)

snobot_sim_java_robot(
    name = "Main",
    main_class = "org.snobotv2.examples.ctre_swerve.Main",
    visibility = ["//examples/test_robot_ctre_swerve:__subpackages__"],
    runtime_deps = [":ctre_swerve"],
)
