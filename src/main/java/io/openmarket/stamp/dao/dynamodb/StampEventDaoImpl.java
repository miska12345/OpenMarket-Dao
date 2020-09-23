package io.openmarket.stamp.dao.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.google.common.collect.ImmutableMap;
import io.openmarket.dynamodb.dao.dynamodb.AbstractDynamoDBDao;
import io.openmarket.stamp.model.StampEvent;
import lombok.NonNull;

import java.util.Optional;

import static io.openmarket.config.StampEventConfig.EVENT_DDB_ATTRIBUTE_ID;
import static io.openmarket.config.StampEventConfig.EVENT_DDB_TABLE_NAME;

public class StampEventDaoImpl extends AbstractDynamoDBDao<StampEvent> implements StampEventDao {
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
    public void update(@NonNull final UpdateItemRequest request) {
        getDbClient().updateItem(request);
    }

    @Override
    public void delete(@NonNull final String eventId) {
        getDbClient().deleteItem(new DeleteItemRequest()
                .withTableName(EVENT_DDB_TABLE_NAME)
                .withKey(ImmutableMap.of(EVENT_DDB_ATTRIBUTE_ID, new AttributeValue(eventId))));
    }
}
