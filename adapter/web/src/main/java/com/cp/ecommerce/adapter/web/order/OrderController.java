package com.cp.ecommerce.adapter.web.order;

import java.util.Optional;

import com.cp.ecommerce.adapter.common.exception.TechnicalProblemException;
import com.cp.ecommerce.adapter.web.order.mapper.OrderWebMapper;
import com.cp.ecommerce.adapter.web.order.resource.OrderResource;
import com.cp.ecommerce.domain.order.Order;
import com.cp.ecommerce.domain.order.usecase.ManageOrderUseCase;
import com.cp.ecommerce.domain.order.usecase.PlaceOrderUseCase;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

/**
 * Controller serving the functionality of {@link Order} API.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final PlaceOrderUseCase placeOrderUseCase;

    private final ManageOrderUseCase manageOrderUseCase;

    private final OrderWebMapper orderWebMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody final OrderResource orderResource) {

        final Order order = orderWebMapper.mapToDomainObject(orderResource).orElse(null);
        Optional.ofNullable(order).ifPresentOrElse(Order::assertValidationsEmpty, () -> {
            throw new TechnicalProblemException("Order data is missing");
        });
        return placeOrderUseCase.placeOrder(order);
    }

    @GetMapping("/{orderNumber}")
    public Order findOrder(@PathVariable("orderNumber") final String orderNumber) {

        final Order order = manageOrderUseCase.findOrder(orderNumber);
        if (Optional.ofNullable(order).isEmpty()) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        return order;
    }

}
