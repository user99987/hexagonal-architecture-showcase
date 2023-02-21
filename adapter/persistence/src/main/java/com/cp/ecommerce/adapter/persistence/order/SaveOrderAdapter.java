package com.cp.ecommerce.adapter.persistence.order;

import com.cp.ecommerce.adapter.common.annotation.PersistenceAdapter;
import com.cp.ecommerce.adapter.persistence.order.entity.OrderEntityRepository;
import com.cp.ecommerce.adapter.persistence.order.mapper.OrderPersistenceMapper;
import com.cp.ecommerce.domain.order.Order;
import com.cp.ecommerce.domain.order.port.outgoing.SaveOrderOutPort;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * Implementation of {@link Order} persistence functionality.
 */
@PersistenceAdapter
@Transactional
@RequiredArgsConstructor
public class SaveOrderAdapter implements SaveOrderOutPort {

    private final OrderPersistenceMapper orderPersistenceMapper;

    private final OrderEntityRepository orderEntityRepository;

    public Order save(final Order order) {

        orderEntityRepository.save(orderPersistenceMapper.mapToEntity(order).orElseThrow());
        return order;
    }

}
