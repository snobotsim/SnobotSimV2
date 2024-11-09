load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

def download_external_archives():
    # Download bzlmodRio <3
    BZLMODRIO_COMMITISH = "7d8a344b5e139e51ff33c9d16c6bb02e2affe7a3"
    BZLMODRIO_SHA256 = "fc1624bfd989071da9f01f91680916d88bd50f2f26ee1238391f1b76a71432ed"
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
