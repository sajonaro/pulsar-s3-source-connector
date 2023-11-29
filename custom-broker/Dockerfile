FROM apachepulsar/pulsar:3.1.1
RUN mkdir s3-connector 
COPY *.jar /s3-connector/
COPY enable_connector.sh    /s3-connector/
RUN chmod +x enable_connector.sh
COPY s3_source_config.yaml   /s3-connector/