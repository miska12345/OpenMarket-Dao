package io.openmarket.config;

public class OrderConfig {
    public static final String ORDER_DDB_TABLE_NAME = "Orders";

    public static final String DDB_ORDER__ATTRIBUTE_BUYER_ID = "buyerId";

    public static final String DDB_ORDER_ATTRIBUTE_SELLER_ID = "sellerId";

    public static final String DDB_ORDER_ATTRIBUTE_TOTAL_AMOUNT = "total";

    public static final String DDB_ORDER_ATTRIBUTE_STATUS = "status";

    public static final String DDB_ORDER_ATTRIBUTE_TRANSACTION_ID = "transactionId";

    public static final String DDB_ORDER_ATTRIBUTE_ORDER_ID = "orderId";

    public static final String DDB_ORDER_ATTRIBUTE_CURRENCY = "currency";

    public static final String DDB_ORDER_ATTRIBUTE_ITEMS = "items";

    public static final String DDB_ORDER_ATTRIBUTE_UPDATED_AT = "lastUpdateAt";

    public static final String DDB_ORDER_ATTRIBUTE_CREATED_AT = "createdAt";

    public static final String DDB_ATTRIBUTE_INDEX_BUYER_ID_TO_CREATED_AT = String.format("%s-%s-index",
            DDB_ORDER__ATTRIBUTE_BUYER_ID, DDB_ORDER_ATTRIBUTE_CREATED_AT);

    public static final String DDB_ATTRIBUTE_INDEX_SELLER_ID_TO_CREATED_AT = String.format("%s-%s-index",
            DDB_ORDER_ATTRIBUTE_SELLER_ID, DDB_ORDER_ATTRIBUTE_CREATED_AT);
}
