package io.openmarket.wallet;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.google.common.collect.ImmutableMap;
import io.openmarket.wallet.dao.dynamodb.WalletDao;
import io.openmarket.wallet.dao.dynamodb.WalletDaoImpl;
import io.openmarket.wallet.model.Wallet;
import io.openmarket.wallet.model.WalletType;

import java.util.Collections;

public class CLI {
    public static void main(String[] args) {
        Wallet wallet = Wallet.builder().coins(Collections.emptyMap()).ownerId("321").build();
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        WalletDao dao = new WalletDaoImpl(client, new DynamoDBMapper(client));
        Wallet wa = Wallet.builder().type(WalletType.USER).ownerId("123").coins(ImmutableMap.of("123", 3.2)).build();
        dao.save(wa);
    }
}
