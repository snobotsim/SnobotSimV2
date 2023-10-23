load("@bzlmodrio//:bzlmodrio_setup.bzl", "bzlmodrio_setup", "get_bzlmodrio_java_dependencies")
load("@rules_jvm_external//:defs.bzl", "maven_install")

def activate_dependencies():
    jupiter_version = "5.6.1"
    platform_version = "1.6.1"

    bzlmodrio_setup()

    maven_artifacts, maven_repositories = get_bzlmodrio_java_dependencies()
    maven_artifacts += [
        "org.junit.jupiter:junit-jupiter-api:" + jupiter_version,
        "org.junit.jupiter:junit-jupiter-params:" + jupiter_version,
        "org.junit.jupiter:junit-jupiter-engine:" + jupiter_version,
        "org.junit.platform:junit-platform-commons:" + platform_version,
        "org.junit.platform:junit-platform-console:" + platform_version,
        "org.junit.platform:junit-platform-engine:" + platform_version,
        "org.junit.platform:junit-platform-launcher:" + platform_version,
        "org.junit.platform:junit-platform-suite-api:" + platform_version,
    ]

    maven_install(
        name = "maven",
        artifacts = maven_artifacts,
        repositories = maven_repositories,
        maven_install_json = "//build_scripts/bazel/deps:maven_install.json",
    )
