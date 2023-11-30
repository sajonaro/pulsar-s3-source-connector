#!/usr/bin/env bash

echo "Deploying Data Generator: Deploying S3 Connector."

bin/pulsar-admin source create \
  --archive s3-connector/snp-s3-source-connector-0.1.0.jar \
  --classname ep.S3SourceConnector \
  --tenant joe_smith_inc \
  --namespace joe_smith_inc_default \
  --name wso_data_source \
  --destination-topic-name wso-data-stream \
  --source-config '{"bucket-name": "contosobucket", "region": "us-east-1" }'

bin/pulsar-admin source list