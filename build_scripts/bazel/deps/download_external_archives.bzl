load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

def download_external_archives():
    # Download Extra java rules
    RULES_JVM_EXTERNAL_TAG = "4.1"
    RULES_JVM_EXTERNAL_SHA = "f36441aa876c4f6427bfb2d1f2d723b48e9d930b62662bf723ddfb8fc80f0140"
    http_archive(
        name = "rules_jvm_external",
        sha256 = RULES_JVM_EXTERNAL_SHA,
        strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
        url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
    )

    # Download Setup python
    http_archive(
        name = "rules_python",
        url = "https://github.com/bazelbuild/rules_python/releases/download/0.5.0/rules_python-0.5.0.tar.gz",
        sha256 = "cd6730ed53a002c56ce4e2f396ba3b3be262fd7cb68339f0377a45e8227fe332",
    )

    # Download PMD
    rules_pmd_version = "0.1.0"
    rules_pmd_sha = "c8839b8e4fb76884632ee001e18a2c311363e208410f1cdd30c78e80aaee25e3"
    http_archive(
        name = "rules_pmd",
        sha256 = rules_pmd_sha,
        strip_prefix = "bazel_rules_pmd-{v}".format(v = rules_pmd_version),
        url = "https://github.com/buildfoundation/bazel_rules_pmd/archive/v{v}.tar.gz".format(v = rules_pmd_version),
    )

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
