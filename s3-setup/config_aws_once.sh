#!/usr/bin/env bash

# ccreate/configure minio profile 
aws configure set profile.minio.aws_access_key_id user
aws configure set profile.minio.aws_secret_access_key password
aws configure set profile.minio.region us-east-1
aws configure set profile.minio.s3.signature_version s3v4