load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

def download_external_archives():
    # Download bzlmodRio <3
    http_archive(
        name = "bzlmodrio",
        url = "https://github.com/bzlmodRio/bzlmodRio/archive/452decb66e79e43ed43e4debbf5d9849ecf6275f.tar.gz",
        sha256 = "6ca2fbcf33da8e769587fb87a8da7736c5088ab0ac1ab4735a99914e5e5fbeda",
        strip_prefix = "bzlmodRio-452decb66e79e43ed43e4debbf5d9849ecf6275f",
    )
    # native.local_repository(
    #     name = "bzlmodrio",
    #     path = "../../bzlmodRio/bzlmodRio",
    # )
