#!/usr/bin/env bash
set -x

export IDEPROBE_DISPLAY=xvfb
export FASTPASS_COURSIER_URL=https://github.com/coursier/coursier/releases/download/v2.0.13/coursier

if [ -z "${TEST_PATTERN}" ]; then
  sbt "$TEST_TARGET/test"
else
  sbt "$TEST_TARGET/testOnly $TEST_PATTERN -- --ignore-runners=none"
fi
