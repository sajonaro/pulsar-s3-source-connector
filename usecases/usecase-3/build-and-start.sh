#!/usr/bin/env bash


#stand up the instrastructure
docker compose up -d

#wait for the infrastructure to be ready
sleep 20

#publish schemas
./publish-schema.sh      
