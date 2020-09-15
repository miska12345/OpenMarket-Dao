package io.openmarket.dagger.module;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class AWSModule {
    @Provides
    @Singleton
    AmazonDynamoDB provideDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard().build();
    }

    @Provides
    @Singleton
    AmazonSQS provideSQS() {
        return AmazonSQSClientBuilder.standard().build();
    }
}
