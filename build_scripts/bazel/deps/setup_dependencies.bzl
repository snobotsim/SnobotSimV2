load("@wpi_bazel_deps//allwpilib:load_allwpilib_from_maven.bzl", "load_allwpilib_from_maven")
load("@wpi_bazel_deps//allwpilib:load_allwpilib_from_source.bzl", "load_allwpilib_from_source")
load("@ctre_bazel_rules//Phoenix:load_ctre_from_maven.bzl", "load_ctre_from_maven")
load("@rev_bazel_rules//REVRobotics:load_rev_from_maven.bzl", "load_rev_from_maven")
load("@navx_bazel_rules//navx_frc:load_navx_from_maven.bzl", "load_navx_from_maven")
load("@wpi_bazel_rules//toolchains:load_toolchains.bzl", "load_toolchains")
load("@rules_pmd//pmd:dependencies.bzl", "rules_pmd_dependencies")

def setup_dependencies(build_from_source = False):
    rules_pmd_dependencies()

    load_ctre_from_maven(version = "5.19.4")

    load_rev_from_maven(version = "1.5.4")

    load_navx_from_maven(version = "4.0.425")

    load_toolchains()

    WPILIB_VERSION = "2021.3.1"

    if build_from_source:
        load_allwpilib_from_source(parent_version = WPILIB_VERSION, git_repository_args = {
            "commit": "bcaa4cbfad2dce661b29fe6bf69cf81b19568ed1",
            "shallow_since": "1627276707 -0400",
            "remote": "https://github.com/bazelRio/allwpilib.git",
        })

    else:
        load_allwpilib_from_maven(version = WPILIB_VERSION)
