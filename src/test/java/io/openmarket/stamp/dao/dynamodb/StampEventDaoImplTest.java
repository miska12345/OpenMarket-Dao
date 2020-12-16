package io.openmarket.stamp.dao.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.local.shared.access.AmazonDynamoDBLocal;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import io.openmarket.stamp.model.EventOwnerType;
import io.openmarket.stamp.model.StampEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static io.openmarket.config.StampEventConfig.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StampEventDaoImplTest {
    private static final String CURRENCY_ID = "DashCoin";
    private static final String OWNER_ID = "miska";
    private static final String OTHER_ID = "beef";

    private static AmazonDynamoDBLocal localDBClient;
    private AmazonDynamoDB dbClient;
    private DynamoDBMapper dbMapper;
    private StampEventDao eventDao;

    @BeforeAll
    public static void setupLocalDB() {
        localDBClient = DynamoDBEmbedded.create();
    }

    @BeforeEach
    public void setup() {
        dbClient = localDBClient.amazonDynamoDB();
        dbMapper = new DynamoDBMapper(dbClient);
        eventDao = new StampEventDaoImpl(dbClient, dbMapper);
        createTable();
    }

    @AfterEach
    public void reset() {
        dbClient.deleteTable(EVENT_DDB_TABLE_NAME);
    }

    @AfterAll
    public static void tearDown() {
        localDBClient.shutdown();
    }

    private void createTable() {
        ProvisionedThroughput throughput = new ProvisionedThroughput(5L, 5L);
        localDBClient.amazonDynamoDB().createTable(new CreateTableRequest().withTableName(EVENT_DDB_TABLE_NAME)
                .withKeySchema(ImmutableList.of(new KeySchemaElement(EVENT_DDB_ATTRIBUTE_ID, KeyType.HASH)))
                .withAttributeDefinitions(new AttributeDefinition(EVENT_DDB_ATTRIBUTE_ID, ScalarAttributeType.S),
                        new AttributeDefinition(EVENT_DDB_ATTRIBUTE_CREATED_AT, ScalarAttributeType.S),
                        new AttributeDefinition(EVENT_DDB_ATTRIBUTE_OWNER_ID, ScalarAttributeType.S))
                .withGlobalSecondaryIndexes(new GlobalSecondaryIndex()
                        .withIndexName(EVENT_DDB_INDEX_OWNER_CREATED_AT)
                        .withKeySchema(new KeySchemaElement()
                                        .withAttributeName(EVENT_DDB_ATTRIBUTE_OWNER_ID)
                                        .withKeyType(KeyType.HASH),
                                new KeySchemaElement()
                                        .withKeyType(KeyType.RANGE)
                                        .withAttributeName(EVENT_DDB_ATTRIBUTE_CREATED_AT))
                        .withProjection(new Projection().withProjectionType(ProjectionType.INCLUDE)
                                .withNonKeyAttributes(EVENT_DDB_ATTRIBUTE_ID))
                        .withProvisionedThroughput(throughput))
                .withProvisionedThroughput(throughput));
    }

    @Test
    public void testLoadEventByID() {
        String eventId = "1";
        eventDao.save(generateEvent(eventId, OWNER_ID, 1.0, 10.0, 0.0));
        final Optional<StampEvent> event = eventDao.load(eventId);
        assertTrue(event.isPresent());
    }

    @Test
    public void testBatchLoadByIdsRegular() {
        Set<String> ids = new HashSet<>();
        for (int i = 0; i < 20; i++) {
            String eventId = String.valueOf(i);
            eventDao.save(generateEvent(eventId, OWNER_ID, 1.0, 10.0, 0.0));
            ids.add(eventId);
        }
        List<StampEvent> events = eventDao.batchLoad(ids);
        assertEquals(20, events.size());
    }

    @Test
    public void testBatchLoadByIdsEmpty() {
        Set<String> ids = ImmutableSet.of("123", "456");
        List<StampEvent> events = eventDao.batchLoad(ids);
        assertTrue(events.isEmpty());
    }

    @Test
    public void testDeleteEventByID() {
        String eventId = "1";
        eventDao.save(generateEvent(eventId, OWNER_ID, 1.0, 10.0, 0.0));
        eventDao.delete(eventId);
        assertEquals(0, dbClient.scan(new ScanRequest().withTableName(EVENT_DDB_TABLE_NAME)).getCount());
    }

    @Test
    public void testGetOwnedEventsRegular() {
        for (int i = 0; i < 20; i++) {
            String eventId = String.valueOf(i);
            String ownerId = i < 10 ? OWNER_ID : OTHER_ID;
            eventDao.save(generateEvent(eventId, ownerId, 1.0, 10.0, 0.0));
        }
        List<String> eventIds = new ArrayList<>();
        Map<String, AttributeValue> exclusiveStartKey = null;
        do {
            exclusiveStartKey = eventDao.getEventIdsByOwner(OWNER_ID, exclusiveStartKey, 5, eventIds);
        } while (exclusiveStartKey != null);
        assertEquals(10, eventIds.size());
        for (int i = 0; i < 10; i++) {
            assertEquals(String.valueOf(i), eventIds.get(i));
        }
    }

    @Test
    public void testGetOwnedEventsEmpty() {
        for (int i = 0; i < 5; i++) {
            String eventId = String.valueOf(i);
            eventDao.save(generateEvent(eventId, OTHER_ID, 1.0, 10.0, 0.0));
        }
        List<String> eventIds = new ArrayList<>();
        Map<String, AttributeValue> exclusiveStartKey = null;
        do {
            exclusiveStartKey = eventDao.getEventIdsByOwner(OWNER_ID, exclusiveStartKey, 5, eventIds);
        } while (exclusiveStartKey != null);
        assertTrue(eventIds.isEmpty());
    }

    private StampEvent generateEvent(String eventId, String ownerId, double rewardAmount,double totalAmount,
                                     double remainingAmount) {
        final Date today = new Date();
        return StampEvent.builder()
                .ownerId(ownerId)
                .eventId(eventId)
                .createdAt(today)
                .rewardAmount(rewardAmount)
                .totalAmount(totalAmount)
                .currencyId(CURRENCY_ID)
                .remainingAmount(remainingAmount)
                .type(EventOwnerType.ORGANIZATION)
                .expireAt(today)
                .build();
    }
}
