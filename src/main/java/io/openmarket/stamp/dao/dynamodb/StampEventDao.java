package io.openmarket.stamp.dao.dynamodb;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import io.openmarket.dynamodb.dao.dynamodb.DynamoDBDao;
import io.openmarket.stamp.model.StampEvent;
import lombok.NonNull;

import java.util.Optional;

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

    /**
     * Get the reward amount for the given event id.
     * @param eventId the event id to get.
     * @return an Optional Double containing the reward amount.
     */
    Optional<Double> getEventRewardAmount(@NonNull final String eventId);

    /**
     * Get the reward total amount for the given event id.
     * @param eventId the event id to get.
     * @return an Optional Double containing the total reward amount.
     */
    Optional<Double> getEventRewardTotalAmount(@NonNull final String eventId);

    /**
     * Get the reward remaining amount for the given event id.
     * @param eventId the event id to get.
     * @return an Optional Double containing the remaining reward amount.
     */
    Optional<Double> getEventRewardRemainAmount(@NonNull final String eventId);
}
