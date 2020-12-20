package io.openmarket.marketplace.dao.sql;
import ch.vorburger.exec.ManagedProcess;
import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import com.google.common.collect.ImmutableList;

import io.openmarket.marketplace.dao.ItemDao;
import io.openmarket.marketplace.dao.ItemDaoImpl;
import io.openmarket.marketplace.model.Item;
import org.junit.jupiter.api.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemDaoImplTest {
    static Connection conn;
    static Properties prop;
    static DB database;
    @BeforeAll
    public static void setupDB() throws IOException, SQLException, ManagedProcessException {
        prop = new Properties();
        prop.load(new FileInputStream("./src/main/java/io/openmarket/marketplace/sql/QueryStatements.properties"));
//
//
//        String dbhost = prop.getProperty("HOST");
////        String user = prop.getProperty("USER");
//        String password = prop.getProperty("PASSWORD");

        //conn = DriverManager.getConnection(dbhost);
        database = DB.newEmbeddedDB(3306);
        database.start();
        conn = DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "");
        Statement createTable = conn.createStatement();
        createTable.execute("CREATE TABLE ITEMS(itemID INT AUTO_INCREMENT , itemName VARCHAR(100) NOT NULL, belongTo VARCHAR(100) NOT NULL, stock INT NOT NULL,purchasedCount INT NOT NULL, itemPrice FLOAT NOT NULL,itemDescription VARCHAR(100) NOT NULL, itemImageLink VARCHAR(100) NOT NULL,itemCategory VARCHAR(100) NOT NULL, itemTag INT NOT NULL,showMarket BOOLEAN NOT NULL, PRIMARY KEY (itemID))");

    }

//    @Test
//    public void testItemDaoConstructor() throws SQLException{
//        ItemDao itemDao = new ItemDaoImpl(this.conn);
//        Statement statement = this.conn.createStatement();
//        statement.execute(this.prop.getProperty("INSERT_ITEM"));
//        List<String> result = itemDao.getItemIdsByOrg("123");
//        for (String res : result) {
//            assertEquals(res, "1");
//        }
//    }


    @Test
    public void testBatchLoad_When_Items_Exist() throws SQLException {
        ItemDao itemDao = new ItemDaoImpl(this.conn);
        Statement statement = this.conn.createStatement();
        statement.execute(this.prop.getProperty("INSERT_ITEM"));
        statement.execute(this.prop.getProperty("INSERT_ITEM"));
        statement.execute(this.prop.getProperty("INSERT_ITEM"));
        List<Item> items = itemDao.batchLoad(ImmutableList.of(1,2,3));
        int count = 1;
        for (Item item : items) {
            assertEquals(item.getItemID(), String.valueOf(count));
            count++;
        }
    }


}
