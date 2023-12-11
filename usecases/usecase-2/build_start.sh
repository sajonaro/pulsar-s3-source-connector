#!/usr/bin/env bash


echo 'create topics:  bucket-names, b1..bn' 
docker compose exec  pulsar-server  bin/pulsar-admin --admin-url http://localhost:8080 \
 topics create persistent://public/default/bucket-names

docker compose exec pulsar-server  bin/pulsar-admin --admin-url http://localhost:8080 \
 topics create persistent://public/default/buck1

docker compose exec  pulsar-server  bin/pulsar-admin --admin-url http://localhost:8080 \
 topics create persistent://public/default/buck2

docker compose exec  pulsar-server  bin/pulsar-admin --admin-url http://localhost:8080 \
 topics create persistent://public/default/buck3
docker compose exec  pulsar-server  bin/pulsar-admin --admin-url http://localhost:8080 \
 topics create persistent://public/default/buck4

docker compose exec  pulsar-server  bin/pulsar-admin --admin-url http://localhost:8080 \
 topics create persistent://public/default/buck5


echo 'create S3 downloaders' 
docker compose exec -i pulsar-server  bin/pulsar-admin  functions create  \
  --jar /pulsar/connectors/s3-download-function-0.0.1.jar \
  --classname ep.S3DownloadFunction \
  --tenant public  \
  --namespace default  \
  --name s3-downloader  \
  --parallelism 3 \
  --inputs public/default/bucket-names \
  --user-config '{"region": "us-east-1", "LOCALSTACK_URL": "http://10.5.0.2:4566", "accessSecret": "password", "accessKey": "user" }' \
  --dead-letter-topic persistent://public/default/dlq-topic \
  --max-message-retries 3 
  


echo 'create & start  Posgresql sink'
docker compose exec -i pulsar-server  bin/pulsar-admin  sinks create  \
  --archive ./connectors/pulsar-io-jdbc-postgres-3.1.1.nar \
  --tenant public  \
  --namespace default  \
  --name db-sink  \
  --sink-config-file /pulsar/conf/sink-config.yaml  \
  --parallelism 1

docker compose exec pulsar-server  \
    bin/pulsar-admin  sinks start \
            --name db-sink 


echo 'check the status of s3 downloader function'
docker compose exec pulsar-server  bin/pulsar-admin functions status --name s3-downloader
