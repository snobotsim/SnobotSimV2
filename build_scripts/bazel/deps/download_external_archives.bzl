load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

def download_external_archives():
    # Download bzlmodRio <3
    http_archive(
        name = "bzlmodrio",
        url = "https://github.com/bzlmodRio/bzlmodRio/archive/5f40922b4a2abb9d1c31fc1e688e336eaca4ac79.tar.gz",
        sha256 = "60cdd1a8e4abd8a903cc6f07be0cea5687b45ad28ebdbf2f35cb625b33c66a0e",
        strip_prefix = "bzlmodRio-5f40922b4a2abb9d1c31fc1e688e336eaca4ac79",
    )
    # native.local_repository(
    #     name = "bzlmodrio",
    #     path = "../../bzlmodRio/bzlmodRio",
    # )
