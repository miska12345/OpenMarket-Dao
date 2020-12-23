package io.openmarket.marketplace.dao;

import io.openmarket.marketplace.model.Item;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

public interface ItemDao {
    /**
     * Get a list of all items owned by the given organization.
     * @param orgId the ID of the organization.
     * @return a List containing item IDs owned by the organization,
     *  or empty list if 1) organization doesn't own any items, 2) failed to fetch from database.
     */
    List<Integer> getItemIdsByOrg(String orgId);

    /**
     * Batch load items for the given list of ItemIDs.
     * @param itemIds a collection of item IDs to load.
     * @param failedIds a collection to store itemIds that failed to fetch.
     * @return a list of items.
     */
    List<Item> batchLoad(Collection<Integer> itemIds, Collection<Integer> failedIds);

    /**
     * Get all items sorted by purchasedCount descendingly
     * @param limit the count of items needed
     * @return list of all items
     */
    List<Item> getAllItemsRankedByPurchasedCount(int limit, String category);

    /**
     * Batch update the item by comparing updated and the one retrieved from itemids
     * @param itemIds item ids that need to be updated
     * @param updateColumn list of int of column index that needs to be update
     * @param updated   list of updated item
     */
    void batchUpdate(@Nonnull final Collection<Integer> itemIds, List<Integer> updateColumn, List<Item> updated);
}
