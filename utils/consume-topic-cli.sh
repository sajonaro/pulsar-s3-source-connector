#!/usr/bin/env sh

docker compose exec -i pulsar-server  bin/pulsar-client consume wso-data-stream -s test-sub1 -n 0 

#docker compose exec -i pulsar-server  bin/pulsar-ctl subscriptions list persistent://public/default/wso-data-stream
