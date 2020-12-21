CREATE TABLE ITEMS(
  itemID INT AUTO_INCREMENT,
  itemName VARCHAR(100) NOT NULL,
  belongTo VARCHAR(100) NOT NULL,
  stock INT NOT NULL,
  purchasedCount INT NOT NULL,
  itemPrice FLOAT NOT NULL,
  itemDescription VARCHAR(100) NOT NULL,
  itemImageLink VARCHAR(100) NOT NULL,
  itemCategory VARCHAR(100) NOT NULL,
  itemTag INT NOT NULL,
  showMarket BOOLEAN NOT NULL,
  PRIMARY KEY(itemID)
  )

  #Get Itemid by Org id
  Select I.itemID From ITEMS As I Where I.belongTo = ?

  #Get Item By Item ids
  Select * From ITEMS As I Where I.itemID = ?

  #Random insert
  INSERT INTO ITEMS (itemName, belongTo, stock, purchasedCount, itemPrice, itemDescription, itemImageLink, itemCategory, itemTag, showMarket)
                        VALUES("name", "123", 100, 1 , 10.0, "This item realy good", "https??", "char", 3, FALSE)

  #Get all items id ranked by count
  Select * From ITEMS As I ORDER BY I.purchasedCount DESC LIMIT ?
