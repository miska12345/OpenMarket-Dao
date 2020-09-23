package io.openmarket.marketplace.dao;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import io.openmarket.dynamodb.dao.dynamodb.AbstractDynamoDBDao;
import io.openmarket.marketplace.model.Item;

import javax.inject.Inject;
import java.util.Optional;

public class ItemDaoImpl extends AbstractDynamoDBDao<Item> implements ItemDao {

    @Inject
    public ItemDaoImpl(AmazonDynamoDB dbClient, DynamoDBMapper mapper) {
        super(dbClient, mapper);
    }

    @Override
    protected boolean validate(Item obj) {
        if (obj.getItemName().isEmpty()) {
            return false;
        } else if (obj.getStock() == 0) {
            return false;
        }
        return true;
    }

    @Override
    public Optional<Item> load(String key) {
        return super.load(Item.class, key);
    }
}
