package com.bang.WebBanHang_Project.exception;

public class OrderCancellationException extends RuntimeException {
    private final String orderId;

    /**
     * Constructs a new OrderCancellationException with the specified detail message.
     *
     * @param message the detail message
     */
    public OrderCancellationException(String message) {
        super(message);
        this.orderId = null;
    }

    /**
     * Constructs a new OrderCancellationException with the specified detail message and order ID.
     *
     * @param message the detail message
     * @param orderId the ID of the order that failed to be cancelled
     */
    public OrderCancellationException(String message, String orderId) {
        super(message);
        this.orderId = orderId;
    }

    /**
     * Constructs a new OrderCancellationException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public OrderCancellationException(String message, Throwable cause) {
        super(message, cause);
        this.orderId = null;
    }

    /**
     * Constructs a new OrderCancellationException with the specified detail message, order ID, and cause.
     *
     * @param message the detail message
     * @param orderId the ID of the order that failed to be cancelled
     * @param cause the cause of the exception
     */
    public OrderCancellationException(String message, String orderId, Throwable cause) {
        super(message, cause);
        this.orderId = orderId;
    }

    /**
     * Returns the ID of the order that failed to be cancelled.
     *
     * @return the order ID, or null if not provided
     */
    public String getOrderId() {
        return orderId;
    }
}
