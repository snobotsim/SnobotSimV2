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
        "//snobot_sim_java/src/main/java/com/revrobotics:simmable_rev",
        "//snobot_sim_java/src/main/java/org/snobotv2/module_wrappers/ctre",
        "//snobot_sim_java/src/main/java/org/snobotv2/module_wrappers/rev",
        "//snobot_sim_java/src/main/java/org/snobotv2/sim_wrappers",
        "//snobot_swerve_sim/src/main/java/edu/wpi/first/wpilibj/simulation/swerve",
        "@bazelrio//libraries/cpp/wpilib/wpiutil:jni",
        "@bazelrio//libraries/java/ctre/phoenix",
        "@bazelrio//libraries/java/rev/revlib",
        "@bazelrio//libraries/java/wpilib/cameraserver",
        "@bazelrio//libraries/java/wpilib/new_commands",
        "@bazelrio//libraries/java/wpilib/wpilibj",
        "@bazelrio//libraries/java/wpilib/wpimath",
        "@bazelrio//libraries/java/wpilib/wpiutil",
    ],
)

snobot_sim_java_robot(
    name = "Main",
    srcs = ["Main.java"],
    main_class = "org.snobotv2.examples.rev_swerve.Main",
    visibility = ["//examples/test_robot_rev_swerve:__subpackages__"],
    deps = [
        ":rev_swerve",
        "@bazelrio//libraries/java/wpilib/wpilibj",
    ],
)
