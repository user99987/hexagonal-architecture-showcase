package com.cp.ecommerce.adapter.common.utils;

import java.util.Date;

import com.cp.ecommerce.domain.order.Order;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Builder class for {@link Order} test data.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderBuilder {

    public static final String TEST_ORDER_NUMBER = "1234";

    public static final String TEST_REMARKS = "remark";

    public static Order mockOrder() {

        return Order.builder()
                .remarks(TEST_REMARKS)
                .orderNumber(TEST_ORDER_NUMBER)
                .created(new Date())
                .customer(CustomerBuilder.mockCustomer())
                .build();
    }

}
