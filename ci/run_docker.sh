#!/usr/bin/env bash

set -x

DOCKER_IMAGE=ideprobe-pants:local
DOCKER_DIRECTORY=/tmp/ideprobe/output
HOST_DIRECTORY=/tmp/ideprobe/output

mkdir -p "${HOST_DIRECTORY}"
docker run  \
  --name pants-tests \
  -e TEST_PATTERN="${TEST_PATTERN}" \
  --mount type=bind,source="${HOST_DIRECTORY}",target="${DOCKER_DIRECTORY}" \
  "${DOCKER_IMAGE}" \
  bash ci/prepare.sh

docker restart pants-tests

docker exec pants-tests "$@"
