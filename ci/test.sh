#!/usr/bin/env bash
set -x

export IDEPROBE_DISPLAY=xvfb

if [ -z "${TEST_PATTERN}" ]; then
  sbt "pantsTests/test"
else
  sbt "pantsTests/testOnly $TEST_PATTERN -- --ignore-runners=none"
fi