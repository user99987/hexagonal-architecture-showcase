package com.cp.ecommerce.domain.order.usecase;

import com.cp.ecommerce.adapter.common.annotation.UseCase;
import com.cp.ecommerce.domain.customer.port.incoming.ManageCustomerInPort;
import com.cp.ecommerce.domain.order.Order;
import com.cp.ecommerce.domain.order.port.incoming.ManageOrderInPort;
import com.cp.ecommerce.domain.order.port.incoming.PlaceOrderInPort;
import com.cp.ecommerce.domain.order.port.incoming.SendMessageInPort;
import com.cp.ecommerce.domain.order.port.outgoing.LogOrderOutPort;
import com.cp.ecommerce.domain.order.port.outgoing.SendEmailOutPort;

import org.apache.logging.log4j.util.Strings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Use case for placing order.
 */
@Slf4j
@RequiredArgsConstructor
@UseCase
public class PlaceOrderUseCase implements PlaceOrderInPort {

    private final ManageOrderInPort manageOrderInPort;

    private final SendMessageInPort sendMessageInPort;

    private final SendEmailOutPort sendEmailOutPort;

    private final LogOrderOutPort logOrderOutPort;

    private final ManageCustomerInPort manageCustomerInPort;

    @Override
    public String placeOrder(final Order order) {

        if (!manageCustomerInPort.checkCustomerExists(order.getCustomer().getContact().getEmail())) {

            log.info("Saving order data started...");
            final Order savedOrder = manageOrderInPort.saveOrder(order);
            log.info("Saving order completed.");

            log.info("Order's number: {}", savedOrder.getOrderNumber());
            logOrderOutPort.log(savedOrder);

            log.info("Starting process of sending confirmation email...");
            sendEmailOutPort.send(savedOrder);
            log.info("Sending confirmation email completed.");

            log.info("Starting process of sending message to queue.");
            sendMessageInPort.sendMessage(savedOrder);
            log.info("Sending message to queue completed.");
            return savedOrder.getOrderNumber();
        } else {

            log.info("Order for current customer already exists.");
            return Strings.EMPTY;
        }
    }

}
