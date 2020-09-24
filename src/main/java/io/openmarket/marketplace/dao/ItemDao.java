package io.openmarket.marketplace.dao;

import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import io.openmarket.dynamodb.dao.dynamodb.DynamoDBDao;
import io.openmarket.marketplace.model.Item;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

public interface ItemDao extends DynamoDBDao<Item> {

    List<String> getItemIdsByOrg(@Nonnull final String orgId);
    void update(UpdateItemRequest request);
    List<Item> batchLoad(@Nonnull final Collection<String> itemIds);
}
