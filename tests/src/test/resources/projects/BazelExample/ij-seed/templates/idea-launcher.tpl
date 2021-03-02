#!/bin/bash
set -x
PROJECT="{BZL_LAUNCHER_PACKAGE}"
mapfile -t < $PROJECT/{WORKSPACE_REF}
WORKSPACE="${MAPFILE[0]}"
install -D -v $PROJECT/.idea/workspace.xml -m 664 "$WORKSPACE"/.idea/workspace.xml
install -D -v $PROJECT/.bazelproject -m 664 "$WORKSPACE"/.bazelproject
IMPORTS=""
for a in "$@"; do IMPORTS+="$a\n  "; done
sed -i.bak "s/!BZL_TARGETS!/$IMPORTS/" "$WORKSPACE"/.bazelproject
IDEA_PATH="idea"
"$IDEA_PATH" "$WORKSPACE"
