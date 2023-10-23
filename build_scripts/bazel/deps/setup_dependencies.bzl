load("@bzlmodrio//:bzlmodrio_dependencies.bzl", "bzlmodrio_dependencies")

def setup_dependencies():
    bzlmodrio_dependencies(
        # toolchain_versions = "2024-1",
        allwpilib_version = "2024.1.1-beta-1",
        # ni_version = "2024.1.1",
        # opencv_version = "2024.4.8.0-1",
        revlib_version = "2024.0.0",
        phoenix_version = "5.32.0-beta-1",
        navx_version = "2024.0.1-beta-2",
        # imgui_version = None,
        # libssh_version = None,
    )
