package io.openmarket.order.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.KeyPair;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.google.common.collect.ImmutableMap;
import io.openmarket.dynamodb.dao.dynamodb.AbstractDynamoDBDao;
import io.openmarket.order.model.Order;
import lombok.NonNull;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.openmarket.config.OrderConfig.*;

public class OrderDaoImpl extends AbstractDynamoDBDao<Order> implements OrderDao {

    @Inject
    public OrderDaoImpl(final AmazonDynamoDB dbClient, DynamoDBMapper dbMapper) {
        super(dbClient, dbMapper);
    }

    @Override
    public Optional<Order> load(@NonNull final String key) {
        return super.load(Order.class, key);
    }

    public Map<String, AttributeValue> getOrderByBuyer (@NonNull final String buyerId, final Map<String,
            AttributeValue> exclusiveStartKey, Collection<String> orderIds) {
        return getOrderByID(buyerId, true, exclusiveStartKey, orderIds);
    }

    public Map<String, AttributeValue> getOrderBySeller (@NonNull final String sellerId, final Map<String,
            AttributeValue> exclusiveStartKey, Collection<String> orderIds) {
        return getOrderByID(sellerId, false, exclusiveStartKey, orderIds);
    }

    public Map<String, AttributeValue> getOrderByID (@NonNull final String id, Boolean isBuyer,
                                                 final Map<String, AttributeValue> exclusiveStartKey,
                                                 Collection<String> orderIds) {
        final QueryRequest request = new QueryRequest()
                .withTableName(ORDER_DDB_TABLE_NAME)
                .withIndexName(isBuyer ? DDB_ATTRIBUTE_INDEX_BUYER_ID_TO_CREATED_AT : DDB_ATTRIBUTE_INDEX_SELLER_ID_TO_CREATED_AT)
                .withKeyConditionExpression("#id = :v")
                .withExpressionAttributeNames(
                        ImmutableMap.of("#id", isBuyer ? DDB_ORDER__ATTRIBUTE_BUYER_ID : DDB_ORDER_ATTRIBUTE_SELLER_ID)
                )
                .withExpressionAttributeValues(
                        ImmutableMap.of(":v", new AttributeValue(id))
                )
                .withExclusiveStartKey(exclusiveStartKey);
        final QueryResult result = super.getDbClient().query(request);
        for (Map<String, AttributeValue> order : result.getItems()) {
            orderIds.add(order.get(DDB_ORDER_ATTRIBUTE_ORDER_ID).getS());
        }
        return result.getLastEvaluatedKey();
    }

    @Override
    public List<Order> batchLoad(@NonNull final Collection<String> orderIds) {
        final List<KeyPair> keyPairs = orderIds.stream()
                .map(a -> new KeyPair().withHashKey(a)).collect(Collectors.toList());
        final Map<String, List<Object>> loadResult = getDbMapper()
                .batchLoad(ImmutableMap.of(Order.class, keyPairs));
        final List<Object> ordersList = loadResult.get(ORDER_DDB_TABLE_NAME);
        if (ordersList == null) {
            return Collections.emptyList();
        }
        return ordersList.stream().map(a -> (Order) a).collect(Collectors.toList());
    }

    @Override
    public boolean validate(@NonNull final Order order) {
        return !order.getOrderId().isEmpty()
                && !order.getBuyerId().isEmpty()
                && !order.getSellerId().isEmpty()
                && order.getTotal() >= 0
                && !order.getCurrency().isEmpty();
    }
}
