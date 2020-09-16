#!/usr/bin/env bash

# script to setup new image (best to do on each intellij version bump)

USERNAME=odisseus
IJ_VERSION=202.6948.69

DOCKER_BUILDKIT=1 BUILDKIT_PROGRESS=plain docker build --tag $USERNAME/ideprobe-pants:$IJ_VERSION -f Dockerfile.deps .
docker push $USERNAME/ideprobe-pants:$IJ_VERSION