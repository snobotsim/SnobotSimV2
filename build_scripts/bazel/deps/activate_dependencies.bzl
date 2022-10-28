load("@rules_jvm_external//:defs.bzl", "maven_install")
load("@rules_pmd//pmd:toolchains.bzl", "rules_pmd_toolchains")

def activate_dependencies():
    PMD_VERSION = "6.39.0"
    rules_pmd_toolchains(pmd_version = PMD_VERSION)

    jupiter_version = "5.6.1"
    platform_version = "1.6.1"

    maven_install(
        name = "maven",
        artifacts = [
            "org.junit.jupiter:junit-jupiter-api:" + jupiter_version,
            "org.junit.jupiter:junit-jupiter-params:" + jupiter_version,
            "org.junit.jupiter:junit-jupiter-engine:" + jupiter_version,
            "org.junit.platform:junit-platform-commons:" + platform_version,
            "org.junit.platform:junit-platform-console:" + platform_version,
            "org.junit.platform:junit-platform-engine:" + platform_version,
            "org.junit.platform:junit-platform-launcher:" + platform_version,
            "org.junit.platform:junit-platform-suite-api:" + platform_version,
            "org.ejml:ejml-simple:0.38",
        ],
        repositories = ["https://repo1.maven.org/maven2"],
        maven_install_json = "//build_scripts/bazel/deps:maven_install.json",
    )
