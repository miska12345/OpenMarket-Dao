package io.openmarket.order.model;

/**
 * The status of the current order.
 */
public enum OrderStatus {
    /**
     * The payment for current order is pending.
     */
    PENDING_PAYMENT,

    /**
     * The payment for current order has been confirmed
     */
    PAYMENT_CONFIRMED,

    /**
     * The order cannot be confirmed because of insufficient balance.
     */
    PAYMENT_NOT_RECEIVED,

    /**
     * The order has been canceled.
     */
    CANCELLED,
}
