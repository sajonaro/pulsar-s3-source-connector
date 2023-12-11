#!/usr/bin/env bash

mvn clean

mvn package

java -cp target/pulsar-json-to-csv-0.0.1.jar ep.JsonToCsvFunction 
