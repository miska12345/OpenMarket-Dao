package io.openmarket.config;

public final class WalletConfig {
    private WalletConfig() {}

    /**
     * ------------------------------------------------------
     * DDB Configurations.
     * ------------------------------------------------------
     */

    /**
     * The DDB table name for Transaction service.
     */
    public static final String WALLET_DDB_TABLE_NAME = "Wallet";

    /**
     * The DDB attribute name for owner of wallet.
     */
    public static final String WALLET_DDB_ATTRIBUTE_OWNER_ID = "OwnerId";

    /**
     * The DDB attribute name for coin id to amount map.
     */
    public static final String WALLET_DDB_ATTRIBUTE_COIN_MAP = "CoinMap";

    /**
     * The DDB attribute name for the type of wallet (either user or org).
     */
    public static final String WALLET_DDB_ATTRIBUTE_TYPE = "WalletType";

    /**
     * The DDB attribute name for the timestamp of when the wallet was last updated.
     */
    public static final String WALLET_DDB_ATTRIBUTE_LAST_UPDATED_AT = "LastUpdatedAt";
}
