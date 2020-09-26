package io.openmarket.order.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.google.common.collect.ImmutableMap;
import io.openmarket.dynamodb.dao.dynamodb.AbstractDynamoDBDao;
import io.openmarket.order.model.Order;
import io.openmarket.organization.dao.OrgDao;
import io.openmarket.organization.model.Organization;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.openmarket.config.OrderConfig.*;

public class OrderDaoImpl extends AbstractDynamoDBDao<Order> implements OrderDao {

    @Inject
    public OrderDaoImpl(AmazonDynamoDB dbClient, DynamoDBMapper dbMapper) {
        super(dbClient, dbMapper);
    }

    @Override
    public Optional<Order> load(String key) {
        return super.load(Order.class, key);
    }

    public List<Order> getOrdersById(String buyerId) {
        List<String> ids = getOrderIdsByBuyer(buyerId);
        List<Order> res = new ArrayList<>();
        for (String id: ids) {
            Optional<Order> opOrder = load(id);
            if (opOrder.isPresent()) res.add(opOrder.get());
        }

        return res;
    }

    public List<String> getOrderIdsByBuyer (String buyerId) {
        QueryRequest request = new QueryRequest().withTableName(ORDER_DDB_TABLE_NAME)
                .withIndexName(ORDER_DDB_INDEX_BUYER_2_ORDERID)
                .withKeyConditionExpression("#id = :v")
                .withExpressionAttributeNames(
                        ImmutableMap.of("#id", ORDER_DDB_ATTRIBUTE_BUYER)
                )
                .withExpressionAttributeValues(
                        ImmutableMap.of(":v", new AttributeValue(buyerId))
                );
        QueryResult result = super.getDbClient().query(request);
        List<String> orderIds = new ArrayList<>();
        for (Map<String, AttributeValue> map: result.getItems()) {
            if (map.containsKey(ORDER_DDB_ATTRIBUTE_ID)) orderIds.add(map.get(ORDER_DDB_ATTRIBUTE_ID).getS());
        }
        return orderIds;
    }


    @Override
    public boolean validate(Order obj) {
        if (obj.getOrderId() != null) {
            return true;
        }
        return false;
    }

    @Override
    public Optional<Order> getOrder(String orderId, String projection) {
        return Optional.empty();
    }
}
