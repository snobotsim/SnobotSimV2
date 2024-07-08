load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

def download_external_archives():
    # Download bzlmodRio <3
    http_archive(
        name = "bzlmodrio",
        url = "https://github.com/bzlmodRio/bzlmodRio/archive/17983128beaeb12f184d22a426fecd8b536a5452.tar.gz",
        sha256 = "aec41ceddbb412c32202ec84246ca5e65fa41325693f8bfc27e2c44c756f66a6",
        strip_prefix = "bzlmodRio-17983128beaeb12f184d22a426fecd8b536a5452",
    )
    # native.local_repository(
    #     name = "bzlmodrio",
    #     path = "../../bzlmodRio/bzlmodRio",
    # )
