#!/usr/bin/env bash

#public/default  here mean  tenant/namespace
docker cp pulsar-server:/pulsar/logs/functions/public/default/s3-downloader/s3-downloader-0.log   .
docker cp pulsar-server:/pulsar/logs/functions/public/default/db-sink/db-sink-0.log .