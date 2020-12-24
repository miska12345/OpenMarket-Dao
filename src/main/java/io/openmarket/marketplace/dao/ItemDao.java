package io.openmarket.marketplace.dao;

import io.openmarket.marketplace.model.Item;
import io.openmarket.mysql.dao.SQLDao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ItemDao extends SQLDao<Item> {
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
    List<Item> batchLoad(List<Integer> itemIds, Collection<Integer> failedIds);

    /**
     * Get all items sorted by purchasedCount descendingly
     * @param limit the count of items needed
     * @return list of all items
     */
    List<Item> getAllItemsRankedByPurchasedCount(int limit, String category);

    /**
     * Update item stock and purchaseCount according to the given map of itemId to stock change.
     * @param itemsToUpdate a {@link List} itemIds that failed to update.
     * @throws IllegalStateException if database cannot be reached.
     */
    List<Integer> updateItemStock(Map<Integer, Integer> itemsToUpdate);
}
