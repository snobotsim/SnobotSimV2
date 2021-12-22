load("@rules_pmd//pmd:dependencies.bzl", "rules_pmd_dependencies")
load("@bazelrio//:defs.bzl", "setup_bazelrio")
load("@bazelrio//:deps.bzl", "setup_bazelrio_dependencies")

def setup_dependencies():
    rules_pmd_dependencies()
    setup_bazelrio_dependencies(
        toolchain_versions = "2021",
        wpilib_version = "2022.1.1-beta-2",
        ni_version = "2022.2.2",
        opencv_version = "4.5.2-1",
        revlib_version = "2022.0.0",
        phoenix_version = "5.20.0-beta-1",
        navx_version = "4.0.435",
    )
    setup_bazelrio()
