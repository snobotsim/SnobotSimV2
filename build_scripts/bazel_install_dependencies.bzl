load("@rules_jvm_external//:defs.bzl", "maven_install")
load("@local_ctre//:ctre.bzl", "third_party_ctre")
load("@local_rev//:rev.bzl", "third_party_rev")
load("@local_navx//:navx.bzl", "third_party_navx")
load("@wpi_bazel_deps//third_party:org_juni_jupiter_junit5.bzl", "get_junit5_maven_deps")
load("@wpi_bazel_deps//third_party:com_fasterxml_jackson_core.bzl", "get_fasterxml_maven_deps")
load("@local_allwpilib//:repo.bzl", "third_party_allwpilib")

def __add(artifacts, repositories, functor):
    new_artifacts, new_repositories = functor()

    return artifacts + new_artifacts, repositories + new_repositories

def install_dependencies():
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
        maven_install_json = "//build_scripts:maven_install.json",
    )
