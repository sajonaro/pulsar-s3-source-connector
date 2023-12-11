#!/usr/bin/env bash

docker compose exec -i pulsar-server  \
    bin/pulsar-admin  --admin-url http://localhost:8080 \
       namespaces grant-permission public/default \
      --actions produce,consume \
      --role '*'


