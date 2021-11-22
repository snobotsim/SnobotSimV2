load("@rules_jvm_external//:defs.bzl", "maven_install")
load("@local_ctre//:ctre.bzl", "third_party_ctre")
load("@local_rev//:rev.bzl", "third_party_rev")
load("@local_navx//:navx.bzl", "third_party_navx")
load("@wpi_bazel_deps//third_party:org_juni_jupiter_junit5.bzl", "get_junit5_maven_deps")
load("@wpi_bazel_deps//third_party:com_fasterxml_jackson_core.bzl", "get_fasterxml_maven_deps")
load("@local_allwpilib//:repo.bzl", "third_party_allwpilib")
load("@rules_pmd//pmd:toolchains.bzl", "rules_pmd_toolchains")
load("@wpi_bazel_rules//toolchains:configure_toolchains.bzl", "configure_toolchains")

def __add(artifacts, repositories, functor):
    new_artifacts, new_repositories = functor()

    return artifacts + new_artifacts, repositories + new_repositories

def activate_dependencies():
    PMD_VERSION = "6.39.0"
    rules_pmd_toolchains(pmd_version = PMD_VERSION)

    configure_toolchains()

    artifacts, repositories = third_party_allwpilib()

    artifacts += get_junit5_maven_deps()
    artifacts += get_fasterxml_maven_deps()

    repositories = ["https://repo1.maven.org/maven2"]

    artifacts, repositories = __add(artifacts, repositories, third_party_ctre)
    artifacts, repositories = __add(artifacts, repositories, third_party_rev)
    artifacts, repositories = __add(artifacts, repositories, third_party_navx)

    maven_install(
        name = "maven",
        artifacts = artifacts,
        repositories = repositories,
        maven_install_json = "//build_scripts/bazel/deps:maven_install.json",
    )
