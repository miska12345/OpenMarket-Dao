package io.openmarket.order.dao.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.local.shared.access.AmazonDynamoDBLocal;
import com.amazonaws.services.dynamodbv2.model.*;
import com.google.common.collect.ImmutableList;
import io.openmarket.order.dao.OrderDaoImpl;
import io.openmarket.order.model.ItemInfo;
import io.openmarket.order.model.Order;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;
import static io.openmarket.config.OrderConfig.ORDER_DDB_ATTRIBUTE_ID;
import static io.openmarket.config.OrderConfig.ORDER_DDB_TABLE_NAME;
import static org.junit.jupiter.api.Assertions.*;


@Log4j2
public class OrderDaoImplTest {
    private static AmazonDynamoDBLocal localDBClient;
    private static AmazonDynamoDB dbClient;
    private DynamoDBMapper dbMapper;
    private OrderDaoImpl orderDao;

    @BeforeAll
    public static void setupLocalDB() {
        localDBClient = DynamoDBEmbedded.create();
    }

    @BeforeEach
    public void setup() {
        dbClient = localDBClient.amazonDynamoDB();
        dbMapper = new DynamoDBMapper(dbClient);
        orderDao = new OrderDaoImpl(dbClient, dbMapper);
        createTable();
    }

    @AfterEach
    public void cleanup() {
        dbClient.deleteTable(ORDER_DDB_TABLE_NAME);
    }

    @Test
    public void save_Order_then_load() {
        Order order = Order.builder()
                .orderId("xsadsasliud")
                .buyer("buyer")
                .seller("seller")
                .delivery_address("addr")
                .total(19293.2)
                .item_summary(ImmutableList.of(
                        ItemInfo.builder().item_id("sasjdoif").price(30.0).quantity(1).build(),
                        ItemInfo.builder().item_id("awefasdfa").price(10.0).quantity(2).build()))
                .build();
        orderDao.save(order);

        ScanResult result = dbClient.scan(new ScanRequest().withTableName(ORDER_DDB_TABLE_NAME));
        assertEquals(1, result.getItems().size());
    }

    @Test
    public void can_Load_Order_when_Exists() {
        Order order = Order.builder()
                .orderId("xsadsasliud")
                .buyer("buyer")
                .seller("seller")
                .delivery_address("addr")
                .total(19293.2)
                .item_summary(ImmutableList.of(
                        ItemInfo.builder().item_id("sasjdoif").price(30.0).quantity(1).build(),
                        ItemInfo.builder().item_id("awefasdfa").price(10.0).quantity(2).build()))
                .build();
        orderDao.save(order);

        Optional<Order> opOrder = orderDao.load("xsadsasliud");
        assertTrue(opOrder.isPresent());

        Order testOrder = opOrder.get();
        assertEquals(testOrder.getBuyer(), order.getBuyer());
        assertEquals(testOrder.getSeller(), order.getSeller());
        assertEquals(testOrder.getOrderId(), order.getOrderId());
        assertTrue(testOrder.getItem_summary().containsAll(order.getItem_summary()));
        assertEquals(testOrder.getDelivery_address(), order.getDelivery_address());
        assertEquals(testOrder.getTotal(), order.getTotal());
    }

    // TODO: FIX THIS
//    @Test
//    public void can_Load_OrderById_when_Exists() {
//        orderDao.save(generateOrder("abcdefg"));
//        orderDao.save(generateOrder("abcdefgh"));
//
//        List<String> res = orderDao.getOrderIdsByBuyer("buyer");
//
//        assertEquals(res.size(), 2);
//        assertTrue(ImmutableList.of("abcdefg", "abcdefgh").containsAll(res));
//
//        List<Order> orders = orderDao.getOrdersById("buyer");
//
//        assertEquals(2, orders.size());
//        System.out.println(orders.get(0));
//        System.out.println(orders.get(1));
//        assertTrue(orders.contains(generateOrder("abcdefg")));
//        assertTrue(orders.contains(generateOrder("abcdefgh")));
//
//    }


    @Test
    public void cannot_Load_If_Not_Exists() {
        Optional<Order> opOrder = orderDao.load("123");
        assertFalse(opOrder.isPresent());
    }

    @AfterAll
    public static void teardown() {
        localDBClient.shutdown();
    }

    private static void createTable() {
        dbClient.createTable(new CreateTableRequest().withTableName(ORDER_DDB_TABLE_NAME)
                .withKeySchema(ImmutableList.of(new KeySchemaElement(ORDER_DDB_ATTRIBUTE_ID, KeyType.HASH)))
                .withAttributeDefinitions(new AttributeDefinition(ORDER_DDB_ATTRIBUTE_ID, ScalarAttributeType.S))
                .withProvisionedThroughput(new ProvisionedThroughput(5L, 5L)));
    }


    private static Order generateOrder(String id) {
        return Order.builder().buyer("buyer")
                .delivery_address("addr")
                .delivery_method("2-day Prime")
                .item_summary(ImmutableList.of(ItemInfo.builder().item_id("oaidjfo")
                        .item_name("jacket")
                        .price(39.99)
                        .quantity(3).build(), ItemInfo.builder().item_id("owoidsi")
                        .quantity(3)
                        .item_name("jeans")
                        .price(19.99).build()))
                .seller("seller")
                .orderId(id)
                .transactionId("daioijfaisdojfioa")
                .build();
    }
}
