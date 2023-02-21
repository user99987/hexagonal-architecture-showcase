package com.cp.ecommerce.domain.order.port.incoming;

import com.cp.ecommerce.domain.order.Order;

/**
 * Place order incoming port.
 */
public interface PlaceOrderInPort {

    /**
     * Place order entry point.
     *
     * @param order {@link Order} object to be processed.
     * @return {@link String} response.
     */
    String placeOrder(final Order order);

}
