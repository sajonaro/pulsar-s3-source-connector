package ep;

import org.joda.time.LocalTime;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.pulsar.io.core.PushSource;
import org.apache.pulsar.io.core.SourceContext;
import org.slf4j.Logger;

public class S3SourceConnector extends PushSource<String> {
    private static Logger logger;
    private static AmazonS3 awsS3client;
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Override
    public void open(Map<String, Object> config, SourceContext sourceContext) throws Exception {
        logger = sourceContext.getLogger();
        logger.info("Starting AWS S3 Source...");

         String bucketName = (String)config.get("bucket-name");
         String secret = (String)config.get("accessSecret");
         String key = (String)config.get("accessKey");
         String region = (String)config.get("region");
         String localstackURL = (String)config.get("LOCALSTACK_URL");


       
        if(localstackURL != ""){
            logger.info(String.format("Localstack URL is: %s", localstackURL));       
            awsS3client = initLocalStackS3Client(key,secret,region,localstackURL);
        }
        else
           awsS3client = initS3Client(key, secret, region);

        Runnable task = new S3LoadRunnableTask(this, sourceContext, awsS3client, bucketName, logger);
        executorService.submit(task);
        
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

	public static void main(String[] args) {
		LocalTime currentTime = new LocalTime();
		System.out.println("Welcome to s3 connector, the current local time is: " + currentTime);
 
	}
}