#!/usr/bin/env bash

set -x

DOCKER_IMAGE=ideprobe-pants:local
DOCKER_DIRECTORY=/tmp/ideprobe/output
HOST_DIRECTORY=/tmp/ideprobe/output

mkdir -p "${HOST_DIRECTORY}"
docker run  \
  --mount type=bind,source="${HOST_DIRECTORY}",target="${DOCKER_DIRECTORY}" \
  -e TEST_PATTERN="${TEST_PATTERN}" \
  "${DOCKER_IMAGE}" \
  "$@"
