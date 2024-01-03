load("@bzlmodrio//private/non_bzlmod:download_dependencies.bzl", "download_dependencies")

def setup_dependencies():
    download_dependencies(
        # toolchain_versions = "2024-1",
        allwpilib_version = "2024.1.1-beta-4",
        # ni_version = "2024.1.1",
        # opencv_version = "2024.4.8.0-1",
        revlib_version = "2024.1.1",
        phoenix_version = "5.32.0-beta-4.bcr1",
        phoenix6_version = "24.0.0-beta-7",
        navx_version = "2024.0.1-beta-4",
        # imgui_version = None,
        # libssh_version = None,
    )
