package io.openmarket.stamp.dao.dynamodb;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import io.openmarket.dynamodb.dao.dynamodb.DynamoDBDao;
import io.openmarket.stamp.model.StampEvent;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * StampEventDao manages the DB operations for StampEvent feature.
 */
public interface StampEventDao extends DynamoDBDao<StampEvent> {

    /**
     * Batch load the given collection of events by eventId.
     * @param eventIdsToLoad the collection containing all eventIds to load in a batch.
     * @return a list containing events with Id contained in eventIdsToLoad, or null if nothing has been loaded.
     */
    List<StampEvent> batchLoad(Collection<String> eventIdsToLoad);

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

    /**
     * Get custom attributes about the given eventId.
     * @param eventId the eventId to get.
     * @param attribute the attribute to get.
     * @return an Optional AttributeValue representing the data in DB.
     */
    Optional<AttributeValue> getCustomAttributes(String eventId, String attribute);

    /**
     * Get the events owned by the given ownerId.
     * @param ownerId the ID of the event owner.
     * @param exclusiveStartKey the exclusiveStartKey from previous query, null means new query.
     * @param count the max number of events to query.
     * @param eventIds a collection to store the the matching event Ids.
     * @return the lastEvaluatedKey that can be used for subsequent queries
     */
    Map<String, AttributeValue> getEventIdsByOwner(String ownerId, Map<String, AttributeValue> exclusiveStartKey,
                                                   int count,
                                                   Collection<String> eventIds);
}
