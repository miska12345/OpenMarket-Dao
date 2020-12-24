package io.openmarket.marketplace.sql;

public class QueryStatements {
    public static final String TABLE_NAME = "ITEMS";
    public static final String CREATE_ITEM_TABLE = "CREATE TABLE ITEMS(\n" +
            "  itemID INT AUTO_INCREMENT,\n" +
            "  itemName VARCHAR(100) NOT NULL,\n" +
            "  belongTo VARCHAR(100) NOT NULL,\n" +
            "  stock INT NOT NULL,\n" +
            "  purchasedCount INT NOT NULL,\n" +
            "  itemPrice FLOAT NOT NULL,\n" +
            "  itemDescription VARCHAR(100) NOT NULL,\n" +
            "  itemImageLink VARCHAR(100) NOT NULL,\n" +
            "  itemCategory VARCHAR(100) NOT NULL,\n" +
            "  itemTag INT NOT NULL,\n" +
            "  showMarket BOOLEAN NOT NULL,\n" +
            "  PRIMARY KEY(itemID)\n" +
            "  )";

    public static final String GET_ITEMID_BY_ORGID = "Select I.itemID From ITEMS As I Where I.belongTo = ?";

    public static final String GET_ITEM_BY_ITEMID = "Select * From ITEMS As I Where I.itemID = ?";

    public static final String GET_ITEM_RANKED_BY_COUNT = "Select * From ITEMS As I Where I.itemCategory like ? ORDER BY I.purchasedCount DESC LIMIT ?";

    public static final String UPDATE_ITEM_STOCK_COUNT = String.format("UPDATE %s SET stock = stock - ?,purchasedCount = purchasedCount + ? WHERE itemID = ? AND stock >= ?", TABLE_NAME);
}
