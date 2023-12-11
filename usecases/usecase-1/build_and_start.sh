#!/usr/bin/env bash

../../connectors/sources/s3-custom-source/mvn clean package

docker compose up -d

./set-login-from-cli.sh

./source-create.sh

./source-start.sh