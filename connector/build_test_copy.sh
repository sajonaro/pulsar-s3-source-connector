#!/usr/bin/env bash

mvn package

java -cp target/snp-s3-source-connector-0.1.0.jar ep.S3SourceConnector

cp target/snp-s3-source-connector-0.1.0.jar ../custom-broker/