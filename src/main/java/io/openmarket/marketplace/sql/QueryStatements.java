package io.openmarket.marketplace.sql;

public class QueryStatements {
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

    public static final String INSERT_ITEM = "INSERT INTO ITEMS (itemName, belongTo, stock, purchasedCount, itemPrice, itemDescription, itemImageLink, itemCategory, itemTag, showMarket) \n" +
            "                        VALUES(\"name\", \"123\", 100, 1 , 10.0, \"This item realy good\", \"https??\", \"char\", 3, FALSE)";

    public static final String INSERT_ITEM2 = "INSERT INTO ITEMS (itemName, belongTo, stock, purchasedCount, itemPrice, itemDescription, itemImageLink, itemCategory, itemTag, showMarket) \n" +
            "                        VALUES(\"name\", \"123\", 100, 10 , 10.0, \"This item realy good\", \"https??\", \"char\", 3, FALSE)";

    public static final String GET_ITEM_RANKED_BY_COUNT = "Select * From ITEMS As I Where I.itemCategory like ? ORDER BY I.purchasedCount DESC LIMIT ?";
}
