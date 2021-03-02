def _idea_launcher_impl(ctx):
    ctx.actions.expand_template(
        template = ctx.file._template,
        output = ctx.outputs._launcher,
        substitutions = {
            "{WORKSPACE_REF}": ctx.file.workspace_ref.basename,
            "{BZL_LAUNCHER_PACKAGE}": ctx.label.package,
        },
        is_executable = True,
    )

def _idea_seed_impl(ctx):
    ctx.actions.expand_template(
        template = ctx.file._template1,
        output = ctx.outputs._workspacexml,
        substitutions = {
            "{LOCATIONHASH}": "random-uuid-goes-here",
        },
    )
    ctx.actions.expand_template(
        template = ctx.file._template2,
        output = ctx.outputs._bazelproject,
        substitutions = {
        },
    )
    return DefaultInfo(
        runfiles = ctx.runfiles(files = [ctx.outputs._workspacexml, ctx.outputs._bazelproject]),
    )

idea_launcher = rule(
    implementation = _idea_launcher_impl,
    attrs = {
        "_template": attr.label(
            default = "templates/idea-launcher.tpl",
            allow_single_file = True,
        ),
        "workspace_ref": attr.label(
            allow_single_file = True,
        ),
    },
    outputs = {
        "_launcher": "launch-idea.sh",
    },
)

idea_seed = rule(
    implementation = _idea_seed_impl,
    attrs = {
        "_template1": attr.label(
            default = "templates/workspace.tpl",
            allow_single_file = True,
        ),
        "_template2": attr.label(
            default = "templates/bazelproject.tpl",
            allow_single_file = True,
        ),
    },
    outputs = {
        "_workspacexml": ".idea/workspace.xml",
        "_bazelproject": ".bazelproject",
    },
)