package io.openmarket.marketplace.dao;

import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import io.openmarket.dynamodb.dao.dynamodb.DynamoDBDao;
import io.openmarket.marketplace.model.Item;

import javax.annotation.Nonnull;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface ItemDao {

    List<Integer> getItemIdsByOrg(@Nonnull final String orgId) throws SQLException;
//    void update(UpdateItemRequest request);
    List<Item> batchLoad(@Nonnull final Collection<Integer> itemIds) throws SQLException;
//    List<String> getItemIdByCategory(@Nonnull final String category, int limit);
}
