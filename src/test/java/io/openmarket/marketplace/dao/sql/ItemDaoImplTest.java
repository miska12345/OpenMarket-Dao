package io.openmarket.marketplace.dao.sql;
import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import com.google.common.collect.ImmutableList;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Ordering;
import com.wix.mysql.EmbeddedMysql;
import io.openmarket.marketplace.dao.ItemDao;
import io.openmarket.marketplace.dao.ItemDaoImpl;
import io.openmarket.marketplace.model.Item;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ItemDaoImplTest {
    private static SessionFactory sessionFactory;
    private static DB database;
    private ItemDao itemDao;

    private static final Item ITEM_A = Item.builder().itemName("Cup").itemID(1).stock(1).purchasedCount(1).belongTo("ABC").itemPrice(10.0).build();
    private static final Item ITEM_B = Item.builder().itemName("Sugar").stock(2).itemID(2).purchasedCount(2).belongTo("ABC").itemPrice(2).build();
    private static final Item ITEM_C = Item.builder().itemName("Tea").stock(10).itemID(3).purchasedCount(3).belongTo("ABC").itemPrice(100.0).build();
    private static final Item ITEM_D = Item.builder().itemName("Gold").stock(0).itemID(4).purchasedCount(100).belongTo("KFC").itemPrice(100000.0).build();

    @BeforeAll
    public static void setupDB() throws ManagedProcessException {
        database = DB.newEmbeddedDB(3306);
        database.start();
    }

    @AfterAll
    public static void shutdownDB() throws ManagedProcessException {
        database.stop();
    }

    @BeforeEach
    public void initialize() {
        Configuration cfg = new Configuration();
        cfg.configure();
        cfg.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/test");
        cfg.setProperty("hibernate.connection.username", "root");
        cfg.setProperty("hibernate.connection.password", "");
        cfg.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        sessionFactory = cfg.buildSessionFactory();
        itemDao = new ItemDaoImpl(sessionFactory);
    }

    @AfterEach
    public void reset() {
        sessionFactory.close();
    }

    @Test
    public void testBatchLoad_When_Items_Exist() {
        List<Item> items = ImmutableList.of(ITEM_A, ITEM_B, ITEM_C);
        fillDBWithItems(items);
        List<Integer> failedItemIds = new ArrayList<>();
        List<Item> loadedItems = itemDao.batchLoad(items.stream().map(Item::getItemID).collect(Collectors.toList()),
                failedItemIds);
        assertEquals(0, failedItemIds.size());
        assertEquals(3, loadedItems.size());
    }

    private void fillDBWithItems(List<Item> items) {
        for (Item item : items) {
            itemDao.save(item);
        }
    }

    @Test
    public void testBatchLoad_When_Item_Not_Exists() {
        List<Integer> failedItemIds = new ArrayList<>();
        List<Item> items = itemDao.batchLoad(ImmutableList.of(1,2,3), failedItemIds);
        assertEquals(3, failedItemIds.size());
    }

    @Test
    public void testGetItemsRankedByCount() {
        fillDBWithItems(ImmutableList.of(ITEM_A, ITEM_B, ITEM_C));
        List<Item> item = itemDao.getAllItemsRankedByPurchasedCount(2, "any");
        assertTrue(Ordering.<Integer> natural().reverse().isOrdered(item.stream().map(Item::getPurchasedCount).collect(Collectors.toList())));
    }

    @Test
    public void testGetItemIdByOrgId() {
        fillDBWithItems(ImmutableList.of(ITEM_A, ITEM_B, ITEM_C, ITEM_D));
        List<Integer> itemIDs = itemDao.getItemIdsByOrg("ABC");
        assertEquals(3, itemIDs.size());
    }


    // TODO: Uncomment the following test for updateItemStock if needed

    @Test
    public void testUpdateItemStock_When_Item_Exists() {
        fillDBWithItems(ImmutableList.of(ITEM_A));
        Map<Integer, Integer> itemsToUpdate = ImmutableMap.of(1, 1);
        List<Integer> failedItemIds = itemDao.updateItemStock(itemsToUpdate);
        assertEquals(0, failedItemIds.size());
        Item itemAUpdated = itemDao.load(1).get();
        assertEquals(ITEM_A.getStock() - 1, itemAUpdated.getStock());
    }

    @Test
    public void testUpdatedItemStock_When_Item_Not_Exist() {
        Map<Integer, Integer> itemsToUpdate = ImmutableMap.of(1, 1);
        List<Integer> failedItemIds = itemDao.updateItemStock(itemsToUpdate);
        assertEquals(1, failedItemIds.size());
        assertEquals(1, failedItemIds.get(0));
    }

    @Test
    public void testUpdatedItemStock_When_Out_Of_Stock() {
        fillDBWithItems(ImmutableList.of(ITEM_D));
        Map<Integer, Integer> itemsToUpdate = ImmutableMap.of(1, 1);
        List<Integer> failedItemIds = itemDao.updateItemStock(itemsToUpdate);
        assertEquals(1, failedItemIds.size());
        assertEquals(1, failedItemIds.get(0));
        Item itemDUpdated = itemDao.load(1).get();
        assertEquals(0, itemDUpdated.getStock());
        assertEquals(ITEM_D.getPurchasedCount(), itemDUpdated.getPurchasedCount());
    }

    @Test
    public void testUpdatedItemStock_Mixed_Items() {
        fillDBWithItems(ImmutableList.of(ITEM_A, ITEM_B, ITEM_D));
        Map<Integer, Integer> itemsToUpdate = ImmutableMap.of(1, 1, 2, 1, 3, 1, 4, 1);
        List<Integer> failedItemIds = itemDao.updateItemStock(itemsToUpdate);
        assertEquals(2, failedItemIds.size());
        assertTrue(failedItemIds.contains(3));
        assertTrue(failedItemIds.contains(4));
    }
}
