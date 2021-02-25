bazelproject_template_path: str = "ij-seed/templates/bazelproject.tpl"
workspace_template_path: str = "ij-seed/templates/workspace.tpl"

bazelproject_output_path: str = ".bazelproject"
workspace_output_path: str = ".idea/workspace.xml"

idea_path: str = "idea"

import sys, shutil, os, subprocess

if len(sys.argv) > 1:
    idea_path = sys.argv[1]

os.makedirs(".idea", exist_ok=True)
shutil.copyfile(bazelproject_template_path, bazelproject_output_path)
shutil.copyfile(workspace_template_path, workspace_output_path)

subprocess.run([idea_path, "."])