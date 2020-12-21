package io.openmarket.order.dao.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.local.shared.access.AmazonDynamoDBLocal;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.google.common.collect.ImmutableList;
import io.openmarket.order.dao.OrderDao;
import io.openmarket.order.dao.OrderDaoImpl;
import io.openmarket.order.model.ItemInfo;
import io.openmarket.order.model.Order;
import io.openmarket.order.model.OrderStatus;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static io.openmarket.config.OrderConfig.*;
import static org.junit.jupiter.api.Assertions.*;


@Log4j2
public class OrderDaoImplTest {
    private static final String ORDER_CURRENCY = "DashCoin";
    private static final String BUYER_ID = "miska";
    private static final String SELLER_ID = "beef";
    private static final String BUYER_ID_2 = "tom";
    private static final String SELLER_ID_2 = "Maple";
    private static final String TRANSAC_ID = "123";
    private static final ItemInfo ITEM_CUP = ItemInfo.builder()
            .quantity(1)
            .itemName("Cup")
            .itemId("123")
            .price(10.0)
            .build();

    private static AmazonDynamoDBLocal localDBClient;
    private AmazonDynamoDB dbClient;
    private DynamoDBMapper dbMapper;
    private OrderDao orderDao;

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
    public void reset() {
        dbClient.deleteTable(ORDER_DDB_TABLE_NAME);
    }

    @AfterAll
    public static void tearDown() {
        localDBClient.shutdown();
    }

    @Test
    public void test_Can_Save_and_Load_Order() {
        String orderId = "123";
        orderDao.save(generateOrder(orderId, BUYER_ID, SELLER_ID, OrderStatus.PAYMENT_CONFIRMED));
        Optional<Order> order = orderDao.load(orderId);
        assertTrue(order.isPresent());
    }

    @ParameterizedTest
    @MethodSource("provideTestGetOrderIdsArgs")
    public void test_Can_Get_OrderIds_By_BuyerId(int numMyItems, int numOtherItems) {
        List<String> orderIds = new ArrayList<>();
        for (int i = 0; i < numMyItems + numOtherItems; i++) {
            orderDao.save(generateOrder(String.valueOf(i), i < numMyItems ? BUYER_ID : BUYER_ID_2, SELLER_ID,
                    OrderStatus.PAYMENT_CONFIRMED));
        }
        orderDao.getOrderByBuyer(BUYER_ID, null, orderIds);
        assertEquals(numMyItems, orderIds.size());
    }

    private static Stream<Arguments> provideTestGetOrderIdsArgs() {
        return Stream.of(
                Arguments.of(10, 0),
                Arguments.of(0, 10),
                Arguments.of(5, 5),
                Arguments.of(0, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestGetOrderIdsArgs")
    public void test_Can_Get_OrderIds_By_SellerId(int numMyItems, int numOtherItems) {
        List<String> orderIds = new ArrayList<>();
        for (int i = 0; i < numMyItems + numOtherItems; i++) {
            orderDao.save(generateOrder(String.valueOf(i), BUYER_ID, i < numMyItems ? SELLER_ID : SELLER_ID_2,
                    OrderStatus.PAYMENT_CONFIRMED));
        }
        orderDao.getOrderBySeller(SELLER_ID, null, orderIds);
        assertEquals(numMyItems, orderIds.size());
    }

    @Test
    public void test_Batch_Load_Orders() {
        List<String> ordersToLoad = ImmutableList.of("1", "2", "3");
        for (String id : ordersToLoad) {
            orderDao.save(generateOrder(id, BUYER_ID, SELLER_ID, OrderStatus.PAYMENT_CONFIRMED));
        }
        List<Order> orders = orderDao.batchLoad(ordersToLoad);
        assertEquals(ordersToLoad.size(), orders.size());
    }

    @Test
    public void test_Batch_Load_Orders_Not_Exist() {
        List<String> ordersToLoad = ImmutableList.of("1", "2", "3");
        List<Order> orders = orderDao.batchLoad(ordersToLoad);
        assertTrue(orders.isEmpty());
    }

    @Test
    public void test_Batch_Load_Orders_Partially_Exist() {
        List<String> ordersToLoad = ImmutableList.of("1", "2", "3");
        for (int i = 0; i < ordersToLoad.size() / 2; i++) {
            orderDao.save(generateOrder(ordersToLoad.get(i), BUYER_ID, SELLER_ID, OrderStatus.PAYMENT_CONFIRMED));
        }
        List<Order> orders = orderDao.batchLoad(ordersToLoad);
        assertEquals(ordersToLoad.size() / 2, orders.size());
    }

    @Test
    public void test_Get_OrderId_By_TransactionId() {
        String orderId = "1";
        orderDao.save(generateOrder(orderId, BUYER_ID, SELLER_ID, OrderStatus.PAYMENT_CONFIRMED));
        Optional<String> optionalOrderId = orderDao.getOrderIdByTransactionId(TRANSAC_ID);
        assertTrue(optionalOrderId.isPresent());
        assertEquals(orderId, optionalOrderId.get());
    }

    @Test
    public void test_Get_OrderId_By_TransactionId_Not_Exist() {
        Optional<String> optionalOrderId = orderDao.getOrderIdByTransactionId(TRANSAC_ID);
        assertFalse(optionalOrderId.isPresent());
    }

    private static Order generateOrder(String orderId, String buyerId, String sellerId, OrderStatus status) {
        return Order.builder()
                .orderId(orderId)
                .currency(ORDER_CURRENCY)
                .buyerId(buyerId)
                .sellerId(sellerId)
                .status(status)
                .transactionId(TRANSAC_ID)
                .total(10.0)
                .items(ImmutableList.of(ITEM_CUP))
                .build();
    }

    private static void createTable() {
        ProvisionedThroughput throughput = new ProvisionedThroughput(5L, 5L);
        localDBClient.amazonDynamoDB().createTable(new CreateTableRequest().withTableName(ORDER_DDB_TABLE_NAME)
                .withKeySchema(ImmutableList.of(new KeySchemaElement(ORDER_DDB_ATTRIBUTE_ORDER_ID, KeyType.HASH)))
                .withAttributeDefinitions(new AttributeDefinition(ORDER_DDB_ATTRIBUTE_ORDER_ID, ScalarAttributeType.S),
                        new AttributeDefinition(ORDER_DDB_ATTRIBUTE_BUYER_ID, ScalarAttributeType.S),
                        new AttributeDefinition(ORDER_DDB_ATTRIBUTE_SELLER_ID, ScalarAttributeType.S),
                        new AttributeDefinition(ORDER_DDB_ATTRIBUTE_CREATED_AT, ScalarAttributeType.S),
                        new AttributeDefinition(ORDER_DDB_ATTRIBUTE_TRANSACTION_ID, ScalarAttributeType.S)
                        )
                .withGlobalSecondaryIndexes(new GlobalSecondaryIndex()
                        .withIndexName(ORDER_DDB_INDEX_BUYER_ID_TO_CREATED_AT)
                        .withKeySchema(new KeySchemaElement()
                                        .withAttributeName(ORDER_DDB_ATTRIBUTE_BUYER_ID)
                                        .withKeyType(KeyType.HASH),
                                new KeySchemaElement()
                                        .withKeyType(KeyType.RANGE)
                                        .withAttributeName(ORDER_DDB_ATTRIBUTE_CREATED_AT))
                        .withProjection(new Projection().withProjectionType(ProjectionType.INCLUDE)
                                .withNonKeyAttributes(ORDER_DDB_ATTRIBUTE_ORDER_ID))
                        .withProvisionedThroughput(throughput),

                        new GlobalSecondaryIndex()
                                .withIndexName(ORDER_DDB_INDEX_SELLER_ID_TO_CREATED_AT)
                                .withKeySchema(new KeySchemaElement()
                                                .withAttributeName(ORDER_DDB_ATTRIBUTE_SELLER_ID)
                                                .withKeyType(KeyType.HASH),
                                        new KeySchemaElement()
                                                .withKeyType(KeyType.RANGE)
                                                .withAttributeName(ORDER_DDB_ATTRIBUTE_CREATED_AT))
                                .withProjection(new Projection().withProjectionType(ProjectionType.INCLUDE)
                                        .withNonKeyAttributes(ORDER_DDB_ATTRIBUTE_ORDER_ID))
                                .withProvisionedThroughput(throughput),

                        new GlobalSecondaryIndex()
                                .withIndexName(ORDER_DDB_INDEX_TRANSACTION_ID_TO_ORDER_ID)
                                .withKeySchema(new KeySchemaElement()
                                                .withAttributeName(ORDER_DDB_ATTRIBUTE_TRANSACTION_ID)
                                                .withKeyType(KeyType.HASH))
                                .withProjection(new Projection().withProjectionType(ProjectionType.INCLUDE)
                                        .withNonKeyAttributes(ORDER_DDB_ATTRIBUTE_ORDER_ID))
                                .withProvisionedThroughput(throughput)
                        )
                .withProvisionedThroughput(throughput));
    }
}
