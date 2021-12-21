load("@rules_pmd//pmd:dependencies.bzl", "rules_pmd_dependencies")
load("@bazelrio//:defs.bzl", "setup_bazelrio")
load("@bazelrio//:deps.bzl", "setup_bazelrio_dependencies")

def setup_dependencies():
    rules_pmd_dependencies()
    setup_bazelrio_dependencies()
    setup_bazelrio()
