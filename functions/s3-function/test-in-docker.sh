#!/usr/bin/env bash

mvn clean package

docker build -t conn .

docker run --network pulsar-poc_pulsar-net conn