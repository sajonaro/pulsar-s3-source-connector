#!/usr/bin/env bash


#publish messages to topic [people] without schema
docker compose exec pulsar-server \
  bin/pulsar-client produce public/default/clients  -m  '{ "name": "John Doe" , "age":"42", "client_id": "1" }'  --separator "-" 
  
docker compose exec pulsar-server \
  bin/pulsar-client produce public/default/clients  -m  '{ "name":"Joe Schmoe" , "age":"50" , "client_id": "2"}'  --separator "-" 



docker compose exec pulsar-server \
  bin/pulsar-client produce public/default/orders  -m  '{  "title":"laptop", "amount":"100", "price":"1000", "customer_id":"1" , "product_id":"11" }'  --separator "-" 

docker compose exec pulsar-server \
  bin/pulsar-client produce public/default/orders  -m  '{  "title":"book", "amount":"10", "price": "10", "customer_id":"1" , "product_id":"12"}'  --separator "-" 

docker compose exec pulsar-server \
  bin/pulsar-client produce public/default/orders  -m  '{  "title":"chewing gum", "amount":"1", "price": "0.5", "customer_id":"2" , "product_id":"13"}'  --separator "-" 
