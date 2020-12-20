package io.openmarket.stamp.dao.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.KeyPair;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.google.common.collect.ImmutableMap;
import io.openmarket.dynamodb.dao.dynamodb.AbstractDynamoDBDao;
import io.openmarket.stamp.model.StampEvent;
import lombok.NonNull;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.openmarket.config.StampEventConfig.*;

public class StampEventDaoImpl extends AbstractDynamoDBDao<StampEvent> implements StampEventDao {
    @Inject
    public StampEventDaoImpl(AmazonDynamoDB dbClient, DynamoDBMapper dbMapper) {
        super(dbClient, dbMapper);
    }

    @Override
    protected boolean validate(@NonNull final StampEvent event) {
        return !event.getOwnerId().isEmpty()
                && !event.getCurrencyId().isEmpty()
                && event.getTotalAmount() > 0
                && event.getRewardAmount() > 0
                && event.getRemainingAmount() >= 0;
    }

    @Override
    public Optional<StampEvent> load(@NonNull final String key) {
        return super.load(StampEvent.class, key);
    }

    @Override
    public List<StampEvent> batchLoad(@NonNull final Collection<String> eventIdsToLoad) {
        final List<KeyPair> keyPairList = new ArrayList<>();
        eventIdsToLoad.forEach(a -> keyPairList.add(new KeyPair().withHashKey(a)));
        final Map<String, List<Object>> batchLoadResult = getDbMapper().batchLoad(ImmutableMap.of(StampEvent.class,
                keyPairList));
        return batchLoadResult.get(EVENT_DDB_TABLE_NAME).stream().map(a -> (StampEvent) a).collect(Collectors.toList());
    }

    @Override
    public void update(@NonNull final UpdateItemRequest request) {
        getDbClient().updateItem(request);
    }

    @Override
    public void delete(@NonNull final String eventId) {
        getDbClient().deleteItem(new DeleteItemRequest()
                .withTableName(EVENT_DDB_TABLE_NAME)
                .withKey(ImmutableMap.of(EVENT_DDB_ATTRIBUTE_ID, new AttributeValue(eventId))));
    }

    @Override
    public Optional<AttributeValue> getCustomAttributes(@NonNull final String eventId, String attribute) {
        final QueryResult result = getDbClient().query(new QueryRequest()
                .withTableName(EVENT_DDB_TABLE_NAME)
                .withKeyConditionExpression("#id = :eid")
                .withExpressionAttributeNames(ImmutableMap.of("#id", EVENT_DDB_ATTRIBUTE_ID))
                .withExpressionAttributeValues(ImmutableMap.of(":eid", new AttributeValue(eventId)))
                .withProjectionExpression(attribute)
                .withLimit(1)
        );
        if (result.getItems().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.getItems().get(0).get(attribute));
    }

    @Override
    public Map<String, AttributeValue> getEventIdsByOwner(@NonNull final String ownerId,
                                                      final Map<String, AttributeValue> exclusiveStartKey,
                                                      final int count,
                                                      @NonNull final  Collection<String> eventIds) {
        final QueryResult result = getDbClient().query(
          new QueryRequest()
                  .withTableName(EVENT_DDB_TABLE_NAME)
                  .withIndexName(EVENT_DDB_INDEX_OWNER_CREATED_AT)
                .withKeyConditionExpression("#ownby = :ownerID")
                .withExpressionAttributeNames(ImmutableMap.of("#ownby", EVENT_DDB_ATTRIBUTE_OWNER_ID))
                .withExpressionAttributeValues(ImmutableMap.of(":ownerID", new AttributeValue(ownerId)))
                  .withExclusiveStartKey(exclusiveStartKey)
                .withLimit(count)
        );
        if (result.getCount() == 0) {
            return null;
        }
        result.getItems().forEach(a -> eventIds.add(a.get(EVENT_DDB_ATTRIBUTE_ID).getS()));
        return result.getLastEvaluatedKey();
    }
}
