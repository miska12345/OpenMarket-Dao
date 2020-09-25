package io.openmarket.order.dao;

import io.openmarket.dynamodb.dao.dynamodb.DynamoDBDao;
import io.openmarket.order.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao extends DynamoDBDao<Order> {
    Optional<Order> getOrder(String orderId, String projection);
    List<Order> getOrdersById(String buyerId);
}
