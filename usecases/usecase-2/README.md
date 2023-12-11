##  Introduction

We download files from s3 bucket via a fleet of functions running in parallel

## High level view
![image](./fan-out-s3.png)



### configure custom s3 downloader
```
docker compose exec -i pulsar-server  bin/pulsar-admin  functions create  \
  --jar /pulsar/connectors/s3-download-function-0.0.1.jar \
  --classname ep.S3DownloadFunction \
  --tenant public  \
  --namespace default  \
  --name s3-downloader  \
  --parallelism 3 \
  --inputs public/default/bucket-names \
  --user-config '{"region": "us-east-1", "LOCALSTACK_URL": "http://10.5.0.2:4566", "accessSecret": "password", "accessKey": "user" }' 

```
### configure build-in Postgresql sink

```
docker compose exec -i pulsar-server  bin/pulsar-admin  sinks create  \
  --archive ./connectors/pulsar-io-jdbc-postgres-3.1.1.nar \
  --tenant public  \
  --namespace default  \
  --name db-sink  \
  --sink-config-file /pulsar/conf/sink-config.yaml  \
  --parallelism 1
```



# Build and Test Locally

### prerequisites
* install docker, docker compose
* install JAVA, Maven

## steps

Open command prompt in root directory of the repository, then:

1. create and run the infrastructure

    ```
    ./build_and_start.sh 
    ```
2. publish bucket name to bucket-names topic e.g.:
    ```
   ./publish-test-cli.sh buck3
    ```
4. check how topic buck3 was populated by io function:
    ```
    ./subscribe-test-cli.sh buck3
    ```
5. Optionally open sink db (via adminer ui) to see how connectors work: http//:localhost:8082


###  Pulsar Visualisations: 
  - Simple Pulsar dashboard:
       http://localhost:80
   
  - Pulsar manager UI: http://localhost:9527 user: admin, login: apachepulsar  (to configure login/password - execute script ./utils/set-login-from-cli.sh)
      BrokerURL - URL where pulsar admin is hosted,in our case: http://pulsar-manager:8080,  Bookie: http://pulsar-manager:8080  (as we run pulsar in standalone mode) 

