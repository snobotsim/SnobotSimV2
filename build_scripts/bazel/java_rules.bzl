load("@rules_bazelrio//:java_rules.bzl", "bazelrio_java_test")
load("@rules_bazelrio//:robot_rules.bzl", "robot_java_binary")
load("@rules_java//java:defs.bzl", "java_library")
load("@rules_pmd//pmd:defs.bzl", "pmd")

def __styleguide(name, srcs, disable_pmd):
    if not disable_pmd:
        pmd(
            name = name + "-pmd_analysis",
            srcs = srcs,
            rulesets = ["//styleguide:pmd_rules"],
        )

def snobot_sim_java_library(name, srcs, disable_pmd = False, **kwargs):
    java_library(
        name = name,
        srcs = srcs,
        **kwargs
    )

    if srcs:
        __styleguide(name, srcs, disable_pmd)

def snobot_sim_java_test(name, srcs, deps = [], runtime_deps = [], disable_pmd = False, **kwargs):
    junit_deps = [
        "@maven//:org_junit_jupiter_junit_jupiter_api",
        "@maven//:org_junit_jupiter_junit_jupiter_params",
        "@maven//:org_junit_jupiter_junit_jupiter_engine",
    ]

    junit_runtime_deps = [
        "@maven//:org_junit_platform_junit_platform_commons",
        "@maven//:org_junit_platform_junit_platform_console",
        "@maven//:org_junit_platform_junit_platform_engine",
        "@maven//:org_junit_platform_junit_platform_launcher",
        "@maven//:org_junit_platform_junit_platform_suite_api",
    ]
    print("Tests are currently disbled for bazel ")

    bazelrio_java_test(
        name = name,
        srcs = srcs,
        deps = deps + junit_deps,
        runtime_deps = runtime_deps + junit_runtime_deps,
        testonly = True,
        main_class = "org.junit.platform.console.ConsoleLauncher",
        args = [
            "--select-package",
            "com",
        ],
        **kwargs
    )

    if srcs:
        __styleguide(name, srcs, disable_pmd)

def snobot_sim_java_robot(name, srcs = [], disable_pmd = False, **kwargs):
    robot_java_binary(
        name = name,
        team_number = 174,
        srcs = srcs,
        halsim_deps = ["@bzlmodrio-allwpilib//libraries/cpp/halsim_gui:halsim_gui"],
        **kwargs
    )

    if srcs:
        __styleguide(name, srcs, disable_pmd)
