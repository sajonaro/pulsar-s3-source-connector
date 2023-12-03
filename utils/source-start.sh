#!/usr/bin/env bash


docker compose exec -i pulsar-server  \
    bin/pulsar-admin  --admin-url http://localhost:8080 \
        sources start \
            --name wso_data_source 
