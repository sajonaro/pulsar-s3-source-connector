package ep;

import org.joda.time.LocalTime;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.pulsar.io.core.PushSource;
import org.apache.pulsar.io.core.SourceContext;
import org.slf4j.Logger;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;


public class S3DownloadFunction implements Function<String, Void> {
  
    private static Logger logger;
    private static AmazonS3 awsS3client;
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);


    @Override
    public Void process(String bucketName, Context context) throws Exception {
        logger = context.getLogger();
        logger.info("s3 function reading from bucket" + bucketName);
        String secret = (String)context.getUserConfigValue("accessSecret").get();
        String key = (String)context.getUserConfigValue("accessKey").get();
        String region = (String)context.getUserConfigValue("region").get();
        String localstackURL = (String)context.getUserConfigValue("LOCALSTACK_URL").get();


        if(localstackURL != ""){
            logger.info(String.format("Localstack URL is: %s", localstackURL));       
            awsS3client = initLocalStackS3Client(key,secret,region,localstackURL);
        }
        else
           awsS3client = initS3Client(key, secret, region);

        Runnable task = new S3LoadRunnableTask(context, awsS3client, bucketName, logger);
        executorService.submit(task);
        return null;
    } 



    @Override
    public void close() throws Exception {
        executorService.shutdown();
        awsS3client.shutdown();
    }

    private AmazonS3 initLocalStackS3Client(String accessKey, String accessSecret, String awsRegion, String endpointURL) {

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, accessSecret);
        
        //.withEndpointConfiguration is used only with localstack
        return  AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpointURL, awsRegion))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    private AmazonS3 initS3Client(String accessKey, String accessSecret, String awsRegion) {

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, accessSecret);
        return  AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(awsRegion)
                .build();
    }


}