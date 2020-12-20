package io.openmarket.dagger.module;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.sqs.AmazonSQS;
import dagger.Module;
import dagger.Provides;
import io.openmarket.account.dynamodb.UserDao;
import io.openmarket.account.dynamodb.UserDaoImpl;
import io.openmarket.dynamodb.dao.sqs.SQSPublisher;
import io.openmarket.marketplace.dao.ItemDao;
import io.openmarket.marketplace.dao.ItemDaoImpl;
import io.openmarket.order.dao.OrderDao;
import io.openmarket.order.dao.OrderDaoImpl;
import io.openmarket.order.model.Order;
import io.openmarket.organization.dao.OrgDao;
import io.openmarket.organization.dao.OrgDaoImpl;
import io.openmarket.stamp.dao.dynamodb.StampEventDao;
import io.openmarket.stamp.dao.dynamodb.StampEventDaoImpl;
import io.openmarket.transaction.dao.dynamodb.TransactionDao;
import io.openmarket.transaction.dao.dynamodb.TransactionDaoImpl;
import io.openmarket.transaction.dao.sqs.SQSTransactionTaskPublisher;
import io.openmarket.transaction.model.TransactionTask;
import io.openmarket.wallet.dao.dynamodb.WalletDao;
import io.openmarket.wallet.dao.dynamodb.WalletDaoImpl;

import javax.inject.Singleton;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Module(includes = AWSModule.class)
public class DaoModule {
    @Provides
    @Singleton
    TransactionDao provideTransactionDao(AmazonDynamoDB dbClient) {
        return new TransactionDaoImpl(dbClient, new DynamoDBMapper(dbClient));
    }

    @Provides
    @Singleton
    OrderDao provideOrderDao(AmazonDynamoDB dbClient) {
        return new OrderDaoImpl(dbClient, new DynamoDBMapper(dbClient));
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
    OrgDao provideOrgDao(AmazonDynamoDB dbClient) {
        return new OrgDaoImpl(dbClient, new DynamoDBMapper(dbClient));
    }


    @Provides
    @Singleton
    ItemDao provideItemDao(Connection conn){
        return new ItemDaoImpl(conn);
    }

    @Provides
    @Singleton
    Connection provideConnection() {
        try {
            return DriverManager.getConnection(System.getenv("DB_URL"));
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    @Provides
    @Singleton
    StampEventDao provideEventDao(AmazonDynamoDB dbClient) {
        return new StampEventDaoImpl(dbClient, new DynamoDBMapper(dbClient));
    }

    @Provides
    @Singleton
    SQSPublisher<TransactionTask> provideSQSTransactionPublisher(AmazonSQS sqsClient) {
        return new SQSTransactionTaskPublisher(sqsClient);
    }
}
