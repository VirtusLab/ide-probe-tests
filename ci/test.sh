#!/usr/bin/env bash
set -x

export IDEPROBE_DISPLAY=xvfb

TEST_PATTERN="com.twitter.intellij.pants.PantsSettingsTest"

if [ -z "${TEST_PATTERN}" ]; then
  sbt "pantsTests/test"
else
  sbt "pantsTests/testOnly $TEST_PATTERN -- --ignore-runners=none"
fi