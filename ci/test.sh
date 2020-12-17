#!/usr/bin/env bash
set -x

export IDEPROBE_DISPLAY=xvfb

if [ -z "${TEST_PATTERN}" ]; then
  sbt "$TEST_TARGET/test"
else
  sbt "$TEST_TARGET/testOnly $TEST_PATTERN -- --ignore-runners=none"
fi
