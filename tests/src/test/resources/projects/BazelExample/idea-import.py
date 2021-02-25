#!/usr/bin/python3
bazelproject_template_path = "ij-seed/templates/bazelproject.tpl"
workspace_template_path = "ij-seed/templates/workspace.tpl"

idea_path = "idea"

import sys, shutil, os, subprocess
from string import Template
import getopt

optlist, args = getopt.getopt(sys.argv[1:], 'l:', ["languages="])
sorted_args = sorted(args)

def get_ijwb_dir_name(targets):
    import hashlib
    hash = hashlib.md5()
    for t in targets:
        hash.update(t.encode("utf-8"))
        dirname = "ijwb_" + hash.hexdigest()[-6:]
        return dirname

bazel_targets = "\n  ".join(sorted_args)
bazel_languages = ""
for o, a in optlist:
    if o in ("-l", "--languages"):
        bazel_languages = "\n  ".join(a.split(','))

ijwb_location = "bazel-imports/" + get_ijwb_dir_name(sorted_args)
os.makedirs(ijwb_location + "/.idea", exist_ok=True)
with open(bazelproject_template_path, "r") as t:
    bazelproject_template = Template(t.read())
bazelproject_output = bazelproject_template.substitute(BZL_TARGETS=bazel_targets, BZL_LANGUAGES=bazel_languages)
with open(ijwb_location + "/.bazelproject", "w") as output:
    output.write(bazelproject_output)
shutil.copyfile(workspace_template_path, ijwb_location + "/.idea/workspace.xml")
print("Working directory: " + os.getcwd())
print("Contents: ")
for l in os.listdir():
    print(l)
print("Running idea at " + ijwb_location)
#sys.exit(4)
subprocess.run([idea_path, ijwb_location])