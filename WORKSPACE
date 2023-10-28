load("//build_scripts/bazel/deps:download_external_archives.bzl", "download_external_archives")

download_external_archives()

load("//build_scripts/bazel/deps:setup_dependencies.bzl", "setup_dependencies")

setup_dependencies()

load("//build_scripts/bazel/deps:activate_dependencies.bzl", "activate_dependencies")

activate_dependencies()

load("@maven//:defs.bzl", "pinned_maven_install")

pinned_maven_install()

load("@rules_wpi_styleguide//dependencies:load_rule_dependencies.bzl", "load_styleguide_rule_dependencies")

load_styleguide_rule_dependencies()

load("@rules_wpi_styleguide//dependencies:load_dependencies.bzl", "load_styleguide_dependencies")

load_styleguide_dependencies()

load("@rules_wpi_styleguide//dependencies:load_transitive_dependencies.bzl", "load_styleguide_transitive_dependencies")

load_styleguide_transitive_dependencies()

load("@rules_wpi_styleguide//dependencies:setup_styleguide.bzl", "setup_styleguide")

setup_styleguide()

load("@rules_wpi_styleguide//dependencies:load_pins.bzl", "load_styleguide_pins")

load_styleguide_pins()
