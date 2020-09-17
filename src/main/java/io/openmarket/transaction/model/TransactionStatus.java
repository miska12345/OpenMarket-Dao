package io.openmarket.transaction.model;

/**
 * The status for transaction.
 */
public enum TransactionStatus {
    /**
     * PENDING - transaction is being processed.
     */
    PENDING,

    /**
     * COMPLETED - transaction has been finalized.
     */
    COMPLETED,

    /**
     * ERROR - an error occurred while processing the transaction.
     */
    ERROR,

    /**
     * REFUND_STARTED - a refund is in progress.
     */
    REFUND_STARTED,

    /**
     * REFUNDED - the transaction has been refunded.
     */
    REFUNDED
}
