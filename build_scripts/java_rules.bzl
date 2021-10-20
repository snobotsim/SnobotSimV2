load("@rules_java//java:defs.bzl", "java_library")
load("@rules_pmd//pmd:defs.bzl", "pmd")
load("@wpi_bazel_rules//rules:java.bzl", "wpilib_junit5_test")
load("@wpi_bazel_deps//rules:wpilibj_robot.bzl", "wpilibj_robot")

def __styleguide(name, srcs):

    pmd(
        name = name + "-pmd_analysis",
        srcs = srcs,
        rulesets = ["//styleguide:pmd_rules"],
    )

def snobot_sim_java_library(name, srcs, **kwargs):
    java_library(
        name = name,
        srcs = srcs,
        **kwargs
    )

    __styleguide(name, srcs)

def snobot_sim_java_test(name, srcs, **kwargs):
    wpilib_junit5_test(
        name = name,
        srcs = srcs,
        **kwargs
    )

    __styleguide(name, srcs)

def snobot_sim_java_robot(name, **kwargs):
    wpilibj_robot(
        name = name,
        **kwargs
    )

    __styleguide(name, srcs = native.glob(["src/main/java/**/*.java", "src/test/java/**/*.java"]))