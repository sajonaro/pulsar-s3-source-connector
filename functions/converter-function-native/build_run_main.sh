#!/usr/bin/env bash

mvn clean

mvn package

java -cp target/pulsar-json-to-csv-0.0.1.jar ep.JsonToCsvFunction '{ "EntityTypeName": "Position", "CorrelationId": "b4f9969b-5570-47f8-bbc4-a12e81dcff23", "col": "val" }'
