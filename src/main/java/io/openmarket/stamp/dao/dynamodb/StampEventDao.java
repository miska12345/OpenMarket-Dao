package io.openmarket.stamp.dao.dynamodb;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import io.openmarket.dynamodb.dao.dynamodb.DynamoDBDao;
import io.openmarket.stamp.model.StampEvent;

/**
 * StampEventDao manages the DB operations for StampEvent feature.
 */
public interface StampEventDao extends DynamoDBDao<StampEvent> {
    /**
     * Execute an {@link UpdateItemRequest}.
     * @param request the request to execute.
     */
    void update(UpdateItemRequest request);

    /**
     * Delete an event from DB.
     * @param eventId the eventId to delete.
     */
    void delete(String eventId);
}
