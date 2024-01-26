#!/usr/bin/env sh

#download the instances of schema  via rest api


echo '\n\r\n\r'

curl -X GET http://localhost:8080/admin/v2/schemas/public/default/people/schema

echo '\n\r\n\r'

curl -X GET http://localhost:8080/admin/v2/schemas/public/default/assets/schema


echo '\n\r\n\r'