load(":ij-seed-rules.bzl", "idea_launcher")
load(":ij-seed-rules.bzl", "idea_seed")
load(":workspace-rules.bzl", "write_workspace_dir")

write_workspace_dir(
    name = "workspace_dir",
)

idea_launcher(
    name = "prepare-idea-launcher",
    workspace_ref = ":workspace_dir",
)

idea_seed(
    name = "prepare-idea-seed",
)

sh_binary(
    name = "launch-idea",
    srcs = ["prepare-idea-launcher"],
    data = ["prepare-idea-seed", "workspace_dir"],
)