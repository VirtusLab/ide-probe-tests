LanguageInfo = provider(fields = ["name"])

def _lang_impl(ctx):
    return LanguageInfo(name = ctx.label.name)

language = rule(
    implementation = _lang_impl,
)
