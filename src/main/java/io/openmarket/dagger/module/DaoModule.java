package io.openmarket.dagger.module;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.sqs.AmazonSQS;
import dagger.Module;
import dagger.Provides;
import io.openmarket.account.dynamodb.UserDao;
import io.openmarket.account.dynamodb.UserDaoImpl;
import io.openmarket.dynamodb.dao.sqs.SQSPublisher;
import io.openmarket.transaction.dao.dynamodb.TransactionDao;
import io.openmarket.transaction.dao.dynamodb.TransactionDaoImpl;
import io.openmarket.transaction.dao.sqs.SQSTransactionTaskPublisher;
import io.openmarket.transaction.model.TransactionTask;
import io.openmarket.wallet.dao.dynamodb.WalletDao;
import io.openmarket.wallet.dao.dynamodb.WalletDaoImpl;

import javax.inject.Singleton;

@Module(includes = AWSModule.class)
public class DaoModule {
    @Provides
    @Singleton
    TransactionDao provideTransactionDao(AmazonDynamoDB dbClient) {
        return new TransactionDaoImpl(dbClient, new DynamoDBMapper(dbClient));
    }

    @Provides
    @Singleton
    WalletDao provideWalletDao(AmazonDynamoDB dbClient) {
        return new WalletDaoImpl(dbClient, new DynamoDBMapper(dbClient));
    }

    @Provides
    @Singleton
    UserDao provideUserDao(AmazonDynamoDB dbClient) {
        return new UserDaoImpl(dbClient, new DynamoDBMapper(dbClient));
    }

    @Provides
    @Singleton
    SQSPublisher<TransactionTask> provideSQSTransactionPublisher(AmazonSQS sqsClient) {
        return new SQSTransactionTaskPublisher(sqsClient);
    }
}
