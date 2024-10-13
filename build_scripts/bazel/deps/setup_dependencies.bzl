load("@bzlmodrio//private/non_bzlmod:download_dependencies.bzl", "download_dependencies")

def setup_dependencies():
    download_dependencies(
        # toolchain_versions = "2024-1",
        allwpilib_version = "2025.1.1-beta-1",
        # ni_version = "2024.1.1",
        # opencv_version = "2024.4.8.0-1",
        revlib_version = "2024.2.4",
        phoenix_version = "5.33.1",
        phoenix6_version = "24.3.0",
        navx_version = "2024.1.0",
        # imgui_version = None,
        # libssh_version = None,
    )
