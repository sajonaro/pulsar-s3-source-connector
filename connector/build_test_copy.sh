#!/usr/bin/env bash

mvn clean

mvn package

java -cp target/s3-source-connector-0.1.0.jar ep.S3SourceConnector

cp target/*.jar ../custom-broker/