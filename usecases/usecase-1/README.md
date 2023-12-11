# Introduction

This project explores Pulsar IO framework. We are building a simple S3 source connector and deploying it into a pulsar cluster,

The connector (source code in ./connector  folder)  is connecting to S3 via AWS s3 SDK, asynchronously reads all files from bucket line by line and pushes every read line into specified topic.

Below command creates (deploys) the connector into pulsar: 
```
docker compose exec -i pulsar-server  bin/pulsar-admin  sources create  \
  --archive /pulsar/connectors/custom-s3-source-connector-0.0.1.jar \
  --classname ep.S3SourceConnector \
  --tenant public  \
  --namespace default  \
  --name wso_data_source  \
  --destination-topic-name wso-data-stream   \
  --source-config '{"bucket-name": "contosobucket", "region": "us-east-1", "LOCALSTACK_URL": "http://10.5.0.2:4566", "accessSecret": "password", "accessKey": "user" }'
 ```




## High level view
![image](./connector_hl.png)


# Build and Test Locally

### prerequisites
* install docker, docker compose
* install JAVA, Maven

## steps

1. Build and run the example :
```
    ./build_and_start.sh
```
2. check the results of connector's job :
 ```
    ./check-topic-cli.sh
 ```

### Visualisations: 
- Simple Pulsar dashboard:
       http://localhost:80
   
- Pulsar manager UI: http://localhost:9527 user: admin, login: apachepulsar  (to configure login/password - execute script ./utils/set-login-from-cli.sh)
      BrokerURL - URL where pulsar admin is hosted,in our case: http://pulsar-manager:8080,  Bookie: http://pulsar-manager:8080  (as we run pulsar in standalone mode) 


