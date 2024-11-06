load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

def download_external_archives():
    # Download bzlmodRio <3
    BZLMODRIO_COMMITISH = "5fc8b394699149fe870963e26ea672d2b00dfa37"
    BZLMODRIO_SHA256 = "b09a5f93efb7231212f87038dfb30146d6ed9f26007349ba2a0c532d3976e675"
    http_archive(
        name = "bzlmodrio",
        url = "https://github.com/bzlmodRio/bzlmodRio/archive/{}.tar.gz".format(BZLMODRIO_COMMITISH),
        sha256 = BZLMODRIO_SHA256,
        strip_prefix = "bzlmodRio-{}".format(BZLMODRIO_COMMITISH),
    )
    # native.local_repository(
    #     name = "bzlmodrio",
    #     path = "../../bzlmodRio/bzlmodRio",
    # )
