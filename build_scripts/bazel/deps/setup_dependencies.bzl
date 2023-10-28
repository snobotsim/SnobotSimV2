load("@bzlmodrio//:bzlmodrio_dependencies.bzl", "bzlmodrio_dependencies")

def setup_dependencies():
    bzlmodrio_dependencies(
        rules_toolchains_version = "2023-7",
        allwpilib_version = "2023.4.3",
        ni_version = "2023.3.0",
        opencv_version = "4.6.0-4",
        revlib_version = "2023.1.3",
        phoenix_version = "5.31.0+23.2.2",
        navx_version = "2023.0.3",
        # imgui_version = None,
        # libssh_version = None,
    )
