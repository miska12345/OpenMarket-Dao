package io.openmarket.config;

public final class StampEventConfig {
    private StampEventConfig() {}

    /**
     * The DDB table name for event feature.
     */
    public static final String EVENT_DDB_TABLE_NAME = "StampEvent";

    /**
     * The DDB attribute name for the unique event Id.
     */
    public static final String EVENT_DDB_ATTRIBUTE_ID = "EventId";

    /**
     * The DDB attribute name for the owner Id.
     */
    public static final String EVENT_DDB_ATTRIBUTE_OWNER_ID = "OwnerId";

    /**
     * The DDB attribute name for the type of owner (e.g. user or organization).
     */
    public static final String EVENT_DDB_ATTRIBUTE_OWNER_TYPE = "OwnerType";

    /**
     * The DDB attribute name for the set of participants who participated the event.
     */
    public static final String EVENT_DDB_ATTRIBUTE_PARTICIPANTS = "Participants";

    /**
     * The DDB attribute name for the timestamp when this event expires.
     */
    public static final String EVENT_DDB_ATTRIBUTE_EXPIRE_AT = "ExpireAt";

    /**
     * The DDB attribute name for the timestamp when this event was created.
     */
    public static final String EVENT_DDB_ATTRIBUTE_CREATED_AT = "CreatedAt";

    /**
     * The DDB attribute name for the currency being awarded in this event.
     */
    public static final String EVENT_DDB_ATTRIBUTE_CURRENCY_ID = "CurrencyId";

    /**
     * The DDB attribute name for the amount that is rewarded.
     */
    public static final String EVENT_DDB_ATTRIBUTE_REWARD_AMOUNT = "RewardAmount";

    /**
     * The DDB attribute name for the total amount that can be rewarded.
     */
    public static final String EVENT_DDB_ATTRIBUTE_TOTAL_AMOUNT = "TotalAmount";

    /**
     * The DDB attribute name for the remaining amount that can be rewarded.
     */
    public static final String EVENT_DDB_ATTRIBUTE_REMAINING_AMOUNT = "RemainingAmount";

    /**
     * The DDB attribute name for the message to show user when he/she has been rewarded.
     */
    public static final String EVENT_DDB_ATTRIBUTE_SUCCESS_MESSAGE = "MessageOnSuccess";

    /**
     * The DDB attribute name for the message to show user when he/she cannot be rewarded.
     */
    public static final String EVENT_DDB_ATTRIBUTE_ERROR_MESSAGE = "MessageOnError";

    /**
     * The DDB index for the owner to createdAt index.
     */
    public static final String EVENT_DDB_INDEX_OWNER_CREATED_AT = String.format("%s-%s-index",
            EVENT_DDB_ATTRIBUTE_OWNER_ID, EVENT_DDB_ATTRIBUTE_CREATED_AT);

    /**
     * --------------------------------------------------------
     * Default Values.
     * --------------------------------------------------------
     */

    /**
     * The default message the user sees when he/she has been rewarded.
     */
    public static final String EVENT_DEFAULT_SUCCESS_MESSAGE = "Success";

    /**
     * The default message the user sees when he/she cannot be rewarded.
     */
    public static final String EVENT_DEFAULT_ERROR_MESSAGE = "Error";
}
