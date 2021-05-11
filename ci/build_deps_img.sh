#!/usr/bin/env bash

# script to setup new image (best to do on each intellij version bump)

USERNAME=odisseus

DOCKER_BUILDKIT=1 BUILDKIT_PROGRESS=plain docker build \
  --tag  $USERNAME/ideprobe-pants-tests:latest \
  --file Dockerfile.deps .