#!/usr/bin/env bash

set -x

DOCKER_IMAGE=ideprobe-gradle:local
DOCKER_DIRECTORY=/tmp/ide-probe/screenshots
HOST_DIRECTORY=/tmp/ide-probe/output

mkdir -p "${HOST_DIRECTORY}"
docker run  \
  --mount type=bind,source="${HOST_DIRECTORY}",target="${DOCKER_DIRECTORY}" \
  "${DOCKER_IMAGE}" \
  "$@"
