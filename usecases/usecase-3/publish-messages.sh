#!/usr/bin/env bash


#publish messages to topic [people] without schema
docker compose exec pulsar-server \
  bin/pulsar-client produce public/default/people  -m  '{\"name\":\"John Doe\" ,\"age\":42}'  --separator "_" 


#publish messages to topic [assets] without schema
docker compose exec pulsar-server \
  bin/pulsar-client produce public/default/assets  -m  '{\"name\":\"Example Asset\",\"description\":\"This is an example asset\",\"tags\":[\"tag1\",\"tag2\"],\"metadata\":{\"createdBy\":\"Joe Schmo\"}}' \
   --separator "_"
