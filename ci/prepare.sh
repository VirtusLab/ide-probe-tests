#!/usr/bin/env bash
set -x

export IDEPROBE_DISPLAY=xvfb

apt install -y zip
sbt "ciSetup/testOnly com.twitter.intellij.pants.PreparePants"
echo "ALL PANTS READY!"
