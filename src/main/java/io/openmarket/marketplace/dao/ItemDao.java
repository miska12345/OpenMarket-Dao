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
    List<Integer> getItemIdsByOrg(@Nonnull final String orgId);

    /**
     * Batch load items for the given list of ItemIDs.
     * @param itemIds a collection of item IDs to load.
     * @return a list of items.
     */
    List<Item> batchLoad(@Nonnull final Collection<Integer> itemIds);
}
