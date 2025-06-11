package com.example.matzipbookserver.global.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.example.matzipbookserver.global.properties.AmazonS3Properties;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AmazonS3Config {

    @Autowired
    private AmazonS3Properties amazonS3Properties;

    @Bean
    public AmazonS3 amazonS3() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(amazonS3Properties.getAccessKey(), amazonS3Properties.getSecretKey());
        return AmazonS3ClientBuilder.standard()
                .withRegion(amazonS3Properties.getRegion())
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}