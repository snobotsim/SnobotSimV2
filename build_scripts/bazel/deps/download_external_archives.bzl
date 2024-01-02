load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

def download_external_archives():
    # Download bzlmodRio <3
    http_archive(
        name = "bzlmodrio",
        url = "https://github.com/bzlmodRio/bzlmodRio/archive/03b41cfcece36d7bcbb984a441803f99f3641e2e.tar.gz",
        sha256 = "c9aae913cd118e79df1b4e9d1387d50f7e47e116330cc281c39e6c0089d3b09d",
        strip_prefix = "bzlmodRio-03b41cfcece36d7bcbb984a441803f99f3641e2e",
    )
    # native.local_repository(
    #     name = "bzlmodrio",
    #     path = "../../bzlmodRio/bzlmodRio",
    # )
