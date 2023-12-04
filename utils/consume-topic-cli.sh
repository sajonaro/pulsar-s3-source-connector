#!/usr/bin/env sh

docker compose exec -i pulsar-server  bin/pulsar-client consume \
  -n 0  \
  -p Earliest \
  -s "test-sub1" \
  wso-data-stream 

