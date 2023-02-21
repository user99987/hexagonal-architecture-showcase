package com.cp.ecommerce.domain.order.port.outgoing;

import com.cp.ecommerce.domain.order.Order;

/**
 * Send email outgoing port.
 */
public interface SendEmailOutPort {

    /**
     * Sending email.
     *
     * @param order placed order.
     */
    void send(final Order order);

}
