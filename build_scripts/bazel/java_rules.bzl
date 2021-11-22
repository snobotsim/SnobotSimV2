load("@rules_java//java:defs.bzl", "java_library")
load("@rules_pmd//pmd:defs.bzl", "pmd")
load("@wpi_bazel_rules//rules:java.bzl", "wpilib_java_binary", "wpilib_junit5_test")
load("@wpi_bazel_rules//rules:halsim_binary.bzl", "wpilib_java_halsim_binary")

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

def snobot_sim_java_test(name, srcs, deps = [], **kwargs):
    wpilib_junit5_test(
        name = name,
        srcs = srcs,
        deps = deps + ["@maven//:org_ejml_ejml_simple"],
        **kwargs
    )

    if srcs:
        __styleguide(name, srcs)

def snobot_sim_java_binary(name, srcs, **kwargs):
    wpilib_java_binary(
        name = name,
        srcs = srcs,
        **kwargs
    )

    if srcs:
        __styleguide(name, srcs)

def snobot_sim_java_robot(name, main_class, srcs = None, visibility = None, deps = None, raw_jni_deps = [], **kwargs):
    halsim_projects_info = {}
    halsim_projects_info["gui"] = ["@local_allwpilib//:halsim_gui"]

    snobot_sim_java_binary(name = name, main_class = main_class, srcs = srcs, deps = deps + ["@maven//:org_ejml_ejml_simple"], visibility = visibility, **kwargs)

    for sim_name, halsim_deps in halsim_projects_info.items():
        sim_target_name = name + ".simulation." + sim_name

        wpilib_java_halsim_binary(
            name = sim_target_name,
            halsim_deps = [],
            srcs = srcs,
            raw_jni_deps = raw_jni_deps + [
                "@local_opencv//:opencv-cpp-shared-libs",
            ],
            wpi_shared_deps = ["@local_allwpilib//:wpilibj-jni_deps"],
            deps = deps + ["@maven//:org_ejml_ejml_simple"],
            main_class = main_class,
            visibility = ["//visibility:public"],
        )
