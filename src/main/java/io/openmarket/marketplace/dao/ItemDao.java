package io.openmarket.marketplace.dao;

import io.openmarket.dynamodb.dao.dynamodb.DynamoDBDao;
import io.openmarket.marketplace.model.Item;

public interface ItemDao extends DynamoDBDao<Item> {

}
