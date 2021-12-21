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

    # Download BazelRio <3
    http_archive(
        name = "bazelrio",
        url = "https://github.com/bazelRio/bazelRio/archive/6dca41c732c637c0f6800ceba8301cf32e6fdec3.tar.gz",
        sha256 = "58e40ae188945997460d372857ab799ac8712a68b9bfc383e713c31a3daf9ce3",
        strip_prefix = "bazelRio-6dca41c732c637c0f6800ceba8301cf32e6fdec3/bazelrio",
    )

    #    native.local_repository(
    #        name = "bazelrio",
    #        path = "C:/Users/PJ/Documents/GitHub/bazelRio/bazelrio/bazelrio"
    #    )

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

    # Download java_rules, Since bazel 5.0 broke backwards compatibility and PMD won't load otherwise
    RULES_JAVA_COMMITISH = "7a3c520737581f13691ad94a0f798a3518d869d1"
    RULES_JAVA_SHA = "bb9c842258f6365edc43c1dda40fc4aa28afa407ba4f6765b784d52f8902dd20"
    http_archive(
        name = "rules_java",
        sha256 = RULES_JAVA_SHA,
        strip_prefix = "rules_java-{}".format(RULES_JAVA_COMMITISH),
        url = "https://github.com/bazelbuild/rules_java/archive/{}.tar.gz".format(RULES_JAVA_COMMITISH),
    )
