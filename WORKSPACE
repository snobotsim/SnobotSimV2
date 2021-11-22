load("//build_scripts/bazel/deps:download_external_archives.bzl", "download_external_archives")

download_external_archives()

load("//build_scripts/bazel/deps:setup_dependencies.bzl", "setup_dependencies")

setup_dependencies()

load("//build_scripts/bazel/deps:activate_dependencies.bzl", "activate_dependencies")

activate_dependencies()

load("@maven//:defs.bzl", "pinned_maven_install")

pinned_maven_install()
