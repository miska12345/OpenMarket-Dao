package io.openmarket.order.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import io.openmarket.dynamodb.dao.dynamodb.AbstractDynamoDBDao;
import io.openmarket.order.model.Order;
import io.openmarket.organization.dao.OrgDao;
import io.openmarket.organization.model.Organization;

import javax.inject.Inject;
import java.util.Optional;

public class OrderDaoImpl extends AbstractDynamoDBDao<Order> implements OrderDao {

    @Inject
    public OrderDaoImpl(AmazonDynamoDB dbClient, DynamoDBMapper dbMapper) {
        super(dbClient, dbMapper);
    }

    @Override
    public Optional<Order> load(String key) {
        return super.load(Order.class, key);
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
