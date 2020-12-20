package io.openmarket.config;

public class MerchandiseConfig {
    public static final String ITEM_DDB_TABLE_NAME = "Items";

    public static final String ITEM_DDB_ATTRIBUTE_ID = "ItemId";

    public static final String ITEM_DDB_ATTRIBUTE_NAME = "ItemName";

    public static final String ITEM_DDB_ATTRIBUTE_PURCHASE_COUNT = "ItemPurchased";

    public static final String ITEM_DDB_ATTRIBUTE_STOCK = "ItemStock";

    public static final String ITEM_DDB_ATTRIBUTE_CREATE_AT = "CreateAt";

    public static final String ITEM_DDB_ATTRIBUTE_BELONG_TO = "OrgId";

    public static final String ITEM_DDB_INDEX_ORGID_2_ITEMID = "OrgIdToItemId-index";

    public static final String ITEM_DDB_ATTRIBUTE_PRICE = "ItemPrice";

    public static final String ITEM_DDB_ATTRIBUTE_DESCRIPTION = "ItemDescription";

    public static final String ITEM_DDB_ATTRIBUTE_IMAGE_LINK = "ItemImageLink";

    public static final String ITEM_DDB_ATTRIBUTE_CATEGORY = "ItemCategory";

    public static final String ITEM_DDB_INDEX_ITEMCATEGORY_ITEMPURCHASED = "ItemCategory-index";

    public static final String QUERY_BY_ORGID = "GET_ITEM_BY_ORGID";

    public static final String QUERY_BY_ITEM_ID = "GET_ITEM_BY_ITEMID";


}
