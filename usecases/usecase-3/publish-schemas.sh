#!/usr/bin/env bash


#create topic [pople] with schema uploaded in ./schemas/ folder (via volume mount into pulsar cluster)
docker compose exec pulsar-server \
   bin/pulsar-admin --admin-url http://localhost:8080 \
   schemas upload persistent://public/default/people --filename /pulsar/conf/json-schema/person-schema.json 

#create topic [assets] with schema uploaded in ./schemas/ folder (via volume mount into pulsar cluster)
docker compose exec pulsar-server \
   bin/pulsar-admin --admin-url http://localhost:8080 \
   schemas upload persistent://public/default/assets --filename /pulsar/conf/json-schema/asset-schema.json 



