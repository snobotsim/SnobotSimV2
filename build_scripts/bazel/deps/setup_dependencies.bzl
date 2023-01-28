load("@rules_pmd//pmd:dependencies.bzl", "rules_pmd_dependencies")
load("@bazelrio//:deps.bzl", "setup_bazelrio_dependencies")

def setup_dependencies():
    rules_pmd_dependencies()
    setup_bazelrio_dependencies(
        # toolchain_versions = "2023-7",
        # wpilib_version = "2023.1.1",
        # # ni_version = "2023.1.0",
        # opencv_version = "4.6.0-4",
        # revlib_version = "2023.1.1",
        # phoenix_version = "5.30.2",
        # navx_version = "2023.0.0",
        # imgui_version = None,
        # libssh_version = None,
    )
