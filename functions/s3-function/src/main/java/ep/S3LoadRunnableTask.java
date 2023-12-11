package ep;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.pulsar.functions.api.Function;
import org.apache.pulsar.functions.api.Context;
import org.slf4j.Logger;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;


public class S3LoadRunnableTask extends Thread {
    private final Logger logger;
    private final AmazonS3 awsS3client;
    private final String bucket;
    private final Context context;

    public S3LoadRunnableTask( Context context, AmazonS3 awsS3client, String bucket, Logger logger) {
        this.context = context;
        this.awsS3client = awsS3client;
        this.bucket = bucket;
        this.logger = logger;
    }

    @Override
    public void run() {
        try {
            logger.info(String.format("Scanning bucket '%s' for files.", bucket));
            ObjectListing objectListing = awsS3client.listObjects(bucket);

            for(S3ObjectSummary os : objectListing.getObjectSummaries()) {
                logger.info(String.format("Processing file %s", os.getKey()));
                consumeFile(os.getKey(), this.context, bucket);
            }
        } catch (Exception e) {
            logger.error( String.format("Error occurred in S3LoadTask, message is %s" ,e.toString()));
        }
    
    }

    private void consumeFile(String fileName, Context context, String publishTopicName ) {
        logger.info(String.format("Consuming file %s to topic %s.", fileName, publishTopicName));
        try {
            List<String> lines = loadFileFromS3(fileName);
            AtomicInteger counter = new AtomicInteger(0);

            //push line by line into topic
            for (String line: lines) {
                context.newOutputMessage(publishTopicName, Schema.STRING).value(line).send();                    
            }
        }catch (PulsarClientException e) {
            logger.error("Filed while processing file '" + fileName + "'.:", e);
            logger.error("details :", e.toString());
        }
    }

    private List<String> loadFileFromS3(String FILE_NAME) {
        try (final S3Object s3Object = awsS3client.getObject(bucket, FILE_NAME);
             final InputStreamReader streamReader = new InputStreamReader(s3Object.getObjectContent(), StandardCharsets.UTF_8);
             final BufferedReader reader = new BufferedReader(streamReader)) {
            return reader.lines().collect(Collectors.toList());
        } catch (final IOException e) {
            logger.warn(e.getMessage());
            return Collections.emptyList();
        }
    }

    private String GenUUID(){
         return UUID.randomUUID().toString().replace("-", "");
    }
}