package io.openmarket.coin.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import io.openmarket.coin.model.Coin;
import io.openmarket.dynamodb.dao.dynamodb.AbstractDynamoDBDao;


import javax.inject.Inject;
import java.util.Optional;

public class CoinDao extends AbstractDynamoDBDao<Coin> {

    @Inject
    public CoinDao(AmazonDynamoDB dbClient, DynamoDBMapper dbMapper) {
        super(dbClient, dbMapper);
    }

    @Override
    public Optional<Coin> load(String key) {
        return super.load(Coin.class, key);
    }

    @Override
    public void save(Coin obj) {
    }

    @Override
    protected boolean validate(Coin obj) {
        return false;
    }

    
}
