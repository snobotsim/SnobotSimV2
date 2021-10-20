load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")

def load_repository_rules():
    # native.local_repository(
    #     name = "wpi_bazel_rules",
    #     # path = "C:/Users/PJ/Documents/GitHub/wpilib/original/bazel_rules/wpi-bazel-rules",
    #     path = "/wpi-bazel-rules",
    # )

    git_repository(
        name = "wpi_bazel_rules",
        commit = "c69cbd8788f7ddf7ab06e01f1d8a2ce54bae9073",
        remote = "https://github.com/bazelRio/wpi-bazel-rules",
        shallow_since = "1627349829 -0400",
    )

    # native.local_repository(
    #     name = "wpi_bazel_deps",
    #     # path = "C:/Users/PJ/Documents/GitHub/wpilib/original/bazel_rules/wpi-bazel-deps-rules",
    #     path = "/wpi-bazel-deps-rules",
    # )

    git_repository(
        name = "wpi_bazel_deps",
        commit = "f73b4513e93f23fe507d26e436e9352cd087cf87",
        remote = "https://github.com/bazelRio/wpi-bazel-deps-rules",
        shallow_since = "1627277567 -0400",
    )

    # native.local_repository(
    #     name = "ctre_bazel_rules",
    #     path = "C:/Users/PJ/Documents/GitHub/wpilib/original/bazel_rules/ctre-bazel-rules",
    # )

    git_repository(
        name = "ctre_bazel_rules",
        commit = "ee9252eda2bb081c8693806643d84dbefa57fe49",
        shallow_since = "1627263792 -0400",
        remote = "https://github.com/bazelRio/ctre-bazel-rules",
    )

    # native.local_repository(
    #     name = "rev_bazel_rules",
    #     path = "C:/Users/PJ/Documents/GitHub/wpilib/original/bazel_rules/rev-bazel-rules",
    # )

    git_repository(
        name = "rev_bazel_rules",
        remote = "https://github.com/bazelRio/rev-bazel-rules",
        commit = "7aea610272a03817d00fdbd23c83ad303c53a1b3",
        shallow_since = "1627264581 -0400",
    )

    # native.local_repository(
    #     name = "navx_bazel_rules",
    #     path = "C:/Users/PJ/Documents/GitHub/wpilib/original/bazel_rules/navx-bazel-rules",
    # )

    git_repository(
        name = "navx_bazel_rules",
        commit = "d37b13b87de22b41a2112549a75413d267d7dd17",
        shallow_since = "1627267733 -0400",
        remote = "https://github.com/bazelRio/navx-bazel-rules",
    )
