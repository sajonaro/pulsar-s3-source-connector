#!/usr/bin/env bash

MINIO_API=http://172.17.0.1:9999
PROFILE=minio
BUCKET_NAME=contosobucket
FILES_PATH=csv-files

aws --endpoint-url $MINIO_API --profile $PROFILE s3 cp $FILES_PATH s3://$BUCKET_NAME/ --recursive