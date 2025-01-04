load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

def download_external_archives():
    # Download bzlmodRio <3
    BZLMODRIO_COMMITISH = "9c184dcd0475c21667db16c60cc3e8ad460d9bd5"
    BZLMODRIO_SHA256 = "2bc897eaa8b11b9c7179cb189a11a66d7ef13cf92591bb8c8a6fb93ea6782bc5"
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
