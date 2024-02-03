#!/usr/bin/env bash

#build the sink function
cd ./sink-function/ &&  ./build_jar.sh  
cd -


#stand up the instrastructure
docker compose up -d

#wait for the infrastructure to be ready
sleep 20


#deploy functions
echo 'create sink function' 
docker compose exec -i pulsar-server  bin/pulsar-admin  functions create  \
  --jar /pulsar/connectors/sink-function-0.0.1.jar \
  --classname ep.DbSinkFunction \
  --tenant public  \
  --namespace default  \
  --name db-sink  \
  --parallelism 1 \
  --inputs public/default/clients,public/default/orders \
  --user-config '{"user": "usr", "password":"1234567", "dbHost":"15.5.0.3",  "port":"5432",  "dbName":"sinkdb", "poolName":"sink-func-pool" }' \
  --dead-letter-topic persistent://public/default/dlq-topic \
  --max-message-retries 3 


#publish schemas
./publish-messages.sh      

#fetch logs
./get-logs.sh