package io.openmarket.marketplace.dao.sql;
import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import com.google.common.collect.ImmutableList;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import io.openmarket.marketplace.dao.ItemDao;
import io.openmarket.marketplace.dao.ItemDaoImpl;
import io.openmarket.marketplace.model.Item;
import io.openmarket.marketplace.sql.QueryStatements;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemDaoImplTest {
    private static ComboPooledDataSource cpds;
    private static DB database;
    private static Statement eraseTableStmt;

    @BeforeAll
    public static void setupDB() throws ManagedProcessException {
        database = DB.newEmbeddedDB(3306);
        database.start();
        cpds = new ComboPooledDataSource();
        cpds.setJdbcUrl("jdbc:mysql://localhost/test");
        cpds.setUser("root");
        cpds.setPassword("");
        cpds.setInitialPoolSize(5);
        cpds.setMinPoolSize(5);
        cpds.setAcquireIncrement(5);
        cpds.setMaxPoolSize(20);
        cpds.setMaxStatements(100);
    }

    @AfterAll
    public static void shutdownDB() throws ManagedProcessException {
        database.stop();
    }

    @BeforeEach
    public void createTable() throws SQLException {
        cpds.getConnection().createStatement().execute("CREATE TABLE ITEMS(itemID INT AUTO_INCREMENT , itemName VARCHAR(100) NOT NULL, belongTo VARCHAR(100) NOT NULL, stock INT NOT NULL,purchasedCount INT NOT NULL, itemPrice FLOAT NOT NULL,itemDescription VARCHAR(100) NOT NULL, itemImageLink VARCHAR(100) NOT NULL,itemCategory VARCHAR(100) NOT NULL, itemTag INT NOT NULL,showMarket BOOLEAN NOT NULL, PRIMARY KEY (itemID))");
    }

    @AfterEach
    public void eraseTable() throws SQLException {
        cpds.getConnection().createStatement().execute("DROP TABLE ITEMS;");
    }

    @Test
    public void testBatchLoad_When_Items_Exist() throws SQLException {
        ItemDao itemDao = new ItemDaoImpl(cpds);
        Statement statement = cpds.getConnection().createStatement();
        statement.execute(QueryStatements.INSERT_ITEM);
        statement.execute(QueryStatements.INSERT_ITEM);
        statement.execute(QueryStatements.INSERT_ITEM);
        List<Item> items = itemDao.batchLoad(ImmutableList.of(1,2,3));
        int count = 1;
        for (Item item : items) {
            assertEquals(item.getItemID(), count);
            count++;
        }
    }

    @Test
    public void testGetItemsRankedByCount() throws SQLException {
        ItemDao itemDao = new ItemDaoImpl(cpds);
        Statement statement = cpds.getConnection().createStatement();
        statement.execute(QueryStatements.INSERT_ITEM2);
        statement.execute(QueryStatements.INSERT_ITEM);
        List<Item> item = itemDao.getAllItemsRankedByPurchasedCount(2, "any");
        assertEquals(item.get(0).getPurchasedCount(),10);
        assertEquals(item.get(1).getPurchasedCount(), 1);
    }
}
