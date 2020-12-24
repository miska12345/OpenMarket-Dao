package io.openmarket.marketplace.dao;

import io.openmarket.marketplace.model.Item;
import io.openmarket.mysql.dao.AbstractMySQLDao;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
public class ItemDaoImpl extends AbstractMySQLDao<Item> implements ItemDao {
    public ItemDaoImpl(@NonNull final SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<Item> load(int id) {
        return super.load(Item.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Integer> getItemIdsByOrg(@NonNull final String orgId) {
        final Session session = getSessionFactory().openSession();
        final Query<Integer> query = session.createQuery("SELECT itemID from Item WHERE belongTo = :orgId");
        query.setParameter("orgId", orgId);

        final List<Integer> result = query.list();
        session.close();
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Item> batchLoad(List<Integer> itemIds, Collection<Integer> failedIds) {
        final String queryPart = " itemID = %d";
        final String joiner = " OR ";
        final StringBuilder sb = new StringBuilder("SELECT i from Item i WHERE");
        for (int i = 0; i < itemIds.size(); i++) {
            int id = itemIds.get(i);
            if (i > 0) {
                sb.append(joiner);
            }
            sb.append(String.format(queryPart, id));
        }
        final Session session = getSessionFactory().openSession();
        final Query query = session.createQuery(sb.toString());
        final List<Item> result = query.list();
        session.close();

        final int diff = itemIds.size() - result.size();
        if (diff > 0) {
            final List<Integer> diffItemIds = new ArrayList<>(itemIds);
            diffItemIds.removeAll(result.stream().map(Item::getItemID).collect(Collectors.toList()));
            failedIds.addAll(diffItemIds);
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Item> getAllItemsRankedByPurchasedCount(int limit, String category) {
        if (category.equals("any")) {
            category = "%";
        }
        final Session session = getSessionFactory().openSession();
        final Query query = session.createQuery("SELECT i FROM Item AS i WHERE itemCategory LIKE :cat ORDER BY purchasedCount desc");
        query.setParameter("cat", category);
        final List<Item> result = query.setMaxResults(limit).list();
        session.close();
        return result;
    }

    @Override
    public List<Integer> updateItemStock(@NonNull final Map<Integer, Integer> itemsToUpdate) {
        final List<Integer> failedItemIDs = new ArrayList<>();
        final Session session = getSessionFactory().openSession();
        for (Map.Entry<Integer, Integer> entry : itemsToUpdate.entrySet()) {
            session.beginTransaction();
            final Query query = session.createQuery("UPDATE Item SET stock = stock - :quantity, purchasedCount = purchasedCount + :quantity WHERE itemID = :id AND stock >= :quantity");
            query.setParameter("quantity", entry.getValue());
            query.setParameter("id", entry.getKey());
            int numRowsChanged = query.executeUpdate();
            session.getTransaction().commit();
            if (numRowsChanged != 1) {
                failedItemIDs.add(entry.getKey());
            }
        }
        session.close();
        return failedItemIDs;
    }
}
