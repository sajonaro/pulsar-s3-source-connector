#!/usr/bin/env sh

#check whats in the output (without consuming the messages)
docker compose exec  pulsar-server \
  bin/pulsar-client consume   public/default/$1   \
    --subscription-type Exclusive \
    --subscription-name $1  \
    --num-messages 0  \
    --subscription-position Earliest
  

