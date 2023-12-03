#!/usr/bin/env bash

mvn clean

mvn package

java -cp target/custom-s3-source-connector-0.0.1.jar ep.S3SourceConnector
