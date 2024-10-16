package com.neuron.cv.config;

import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AWSConfiguration {

	@Value("${storj.accessKeyId}")
	private String accessKey;

	@Value("${storj.secretKey}")
	private String secretKey;

	@Value("${storj.region}")
	private String region;
	
	@Value("${storj.endpointUrl}")
	private String endPoint;


	@Bean
	public S3Client amazonS3Client() {
	  
	  AwsCredentialsProvider creds =
          StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey,
              secretKey));
	  
	  URI endpoint = URI.create(endPoint);
	 S3Client s3Client = S3Client.builder().region(Region.US_EAST_1)
	      .endpointOverride(endpoint).credentialsProvider(creds).build();
	 return s3Client;
	}
}
