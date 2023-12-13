#!/usr/bin/env bash

#(re)build s3-download-function
cd ../../connectors/sources/s3-custom-source &&  mvn clean package
cd -
#stand up the instrastructure
docker compose up -d

