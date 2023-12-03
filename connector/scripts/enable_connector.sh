#!/usr/bin/env bash

echo "Deploying Data Generator: Deploying S3 Connector."

bin/pulsar-admin  sources create  \
  --archive /pulsar/connectors/custom-s3-source-connector-0.0.1.jar \
  --classname ep.S3SourceConnector \
  --tenant public  \
  --namespace default  \
  --name wso_data_source  \
  --destination-topic-name wso-data-stream   \
  --source-config '{"bucket-name": "contosobucket", "region": "us-east-1" }' 

bin/pulsar-admin source list