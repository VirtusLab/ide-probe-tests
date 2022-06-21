#!/usr/bin/env bash

set -x

DOCKER_IMAGE=ideprobe-tests:local
DOCKER_DIRECTORY=/tmp/ide-probe/output
HOST_DIRECTORY=/tmp/ide-probe/output

mkdir -p "${HOST_DIRECTORY}"
docker run  \
  --mount type=bind,source="${HOST_DIRECTORY}",target="${DOCKER_DIRECTORY}" \
  -e TEST_PATTERN="${TEST_PATTERN}" \
  -e TEST_TARGET="${TEST_TARGET}" \
  "${DOCKER_IMAGE}" \
  "$@"
