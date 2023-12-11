#!/usr/bin/env sh

#publish  bucket name to input topic
docker compose exec pulsar-server \
  bin/pulsar-client produce public/default/bucket-names  -m  $1
  

