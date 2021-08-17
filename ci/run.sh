#!/usr/bin/env bash
set -x
export IDEPROBE_DISPLAY=xvfb
sbt "pantsTests/test"
