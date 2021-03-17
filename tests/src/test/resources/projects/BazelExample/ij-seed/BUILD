load(":ij-seed-rules.bzl", "idea_launcher")
load(":ij-seed-rules.bzl", "idea_seed")
load(":workspace-rules.bzl", "write_workspace_dir")
load("@bazel_skylib//rules:common_settings.bzl", "string_flag")
load(":custom-flag-rules.bzl", "language")

write_workspace_dir(
    name = "workspace_dir",
)

idea_launcher(
    name = "prepare-idea-launcher",
    workspace_ref = ":workspace_dir",
)

idea_seed(
    name = "prepare-idea-seed",
    language = select({
        ":lang-scala": ":scala",
        ":lang-dart": ":dart",
        ":lang-python": ":python",
        "//conditions:default": "java"
    }),
)

sh_binary(
    name = "launch-idea",
    srcs = ["prepare-idea-launcher"],
    data = ["prepare-idea-seed", "workspace_dir"],
)

string_flag(name = "language", build_setting_default = "lang-python")

config_setting(
    name = "lang-scala",
    flag_values = {
        ":language": "scala",
    }
)

config_setting(
    name = "lang-dart",
    flag_values = {
        ":language": "dart",
    }
)

config_setting(
    name = "lang-python",
    flag_values = {
        ":language": "python",
    }
)

language(name = "scala")
language(name = "dart")
language(name = "python")
language(name = "java")