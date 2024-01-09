load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

def download_external_archives():
    # Download bzlmodRio <3
    http_archive(
        name = "bzlmodrio",
        url = "https://github.com/bzlmodRio/bzlmodRio/archive/8962df6bad4c1d6a9d23d917c36c868deca75b42.tar.gz",
        sha256 = "d845a46533f7dfcba4819472392e538f8c43bc3c505a0c98e358e0f0b8d868e1",
        strip_prefix = "bzlmodRio-8962df6bad4c1d6a9d23d917c36c868deca75b42",
    )
    # native.local_repository(
    #     name = "bzlmodrio",
    #     path = "../../bzlmodRio/bzlmodRio",
    # )
