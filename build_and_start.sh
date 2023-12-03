#!/usr/bin/env bash

cd ./connector 

./build_test_copy.sh 

cd ..

docker compose up -d

cd ./utils

./set-login-from-cli.sh

./source-create.sh

./source-start.sh