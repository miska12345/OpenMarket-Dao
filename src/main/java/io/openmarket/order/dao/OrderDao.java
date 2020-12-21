package io.openmarket.order.dao;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import io.openmarket.dynamodb.dao.dynamodb.DynamoDBDao;
import io.openmarket.order.model.Order;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface OrderDao extends DynamoDBDao<Order> {
    /**
     * Get order by buyerId.
     * @param buyerId the buyerId to get order for.
     * @param exclusiveStartKey the exclusiveStartKey to use.
     * @param orderIds a Collection to store the result.
     * @return a lastEvaluatedKey for paginated query.
     */
    Map<String, AttributeValue> getOrderByBuyer (String buyerId, Map<String,
            AttributeValue> exclusiveStartKey, Collection<String> orderIds);

    /**
     * Get order by sellerId.
     * @param sellerId the sellerId.
     * @param exclusiveStartKey the exclusiveStartKey to use.
     * @param orderIds a Collection to store the result.
     * @return a lastEvaluateKey for paginated query.
     */
    Map<String, AttributeValue> getOrderBySeller (String sellerId, Map<String,
            AttributeValue> exclusiveStartKey, Collection<String> orderIds);

    /**
     * Batch load the given orders by ID.
     * @param orderIds a collection of orderIds to load in a batch.
     * @return a List of orders loaded.
     */
    List<Order> batchLoad(Collection<String> orderIds);
}
