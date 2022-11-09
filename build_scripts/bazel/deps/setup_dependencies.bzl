load("@rules_pmd//pmd:dependencies.bzl", "rules_pmd_dependencies")
load("@bazelrio//:defs.bzl", "setup_bazelrio")
load("@bazelrio//:deps.bzl", "setup_bazelrio_dependencies")

def setup_dependencies():
    rules_pmd_dependencies()
    setup_bazelrio_dependencies(
        toolchain_versions = "2023-5",
        wpilib_version = "2023.1.1-beta-3",
        ni_version = "2023.1.0",
        opencv_version = "4.6.0-2",
        revlib_version = "2023.0.0",
        phoenix_version = "5.30.1-beta-2",
        navx_version = "2023.1.0-beta-2",
        imgui_version = None,
        libssh_version = None,
    )
    setup_bazelrio()
