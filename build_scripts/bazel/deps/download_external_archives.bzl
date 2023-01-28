load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

def download_external_archives():
    # Download Extra java rules
    RULES_JVM_EXTERNAL_TAG = "4.4.2"
    RULES_JVM_EXTERNAL_SHA = "735602f50813eb2ea93ca3f5e43b1959bd80b213b836a07a62a29d757670b77b"
    http_archive(
        name = "rules_jvm_external",
        sha256 = RULES_JVM_EXTERNAL_SHA,
        strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
        url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
    )

    # Download BazelRio <3
    # http_archive(
    #     name = "bazelrio",
    #     url = "https://github.com/bazelRio/bazelRio/archive/refs/tags/0.10.0.zip",
    #     sha256 = "18b109dbd5204910600823e6c9ff405fa7ed7c43d0a78f24077f8187311745a9",
    #     strip_prefix = "bazelRio-0.10.0/bazelrio",
    # )
    http_archive(
        name = "bazelrio",
        url = "https://github.com/bzlmodRio/bzlmodRio/archive/6472523adf3bc9570410079028762bc207842beb.tar.gz",
        sha256 = "fda87eed3225b048eff5c045b6aea7e8373dd5cadefd85f934b0b5409c720a94",
        strip_prefix = "bzlmodRio-6472523adf3bc9570410079028762bc207842beb",
    )
    # native.local_repository(
    #     name = "bazelrio",
    #     path = "../../bzlmodRio/bzlmodRio"
    # )

    # Download java_rules, Since bazel 5.0 broke backwards compatibility and PMD won't load otherwise
    RULES_JAVA_COMMITISH = "5.3.5"
    RULES_JAVA_SHA = "7df0811e29830e79be984f9d5bf6839ce151702d694038126d7c23296785bf97"
    http_archive(
        name = "rules_java",
        sha256 = RULES_JAVA_SHA,
        strip_prefix = "rules_java-{}".format(RULES_JAVA_COMMITISH),
        url = "https://github.com/bazelbuild/rules_java/archive/{}.tar.gz".format(RULES_JAVA_COMMITISH),
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
