package com.cp.ecommerce.adapter.persistence.customer;

import com.cp.ecommerce.adapter.common.annotation.PersistenceAdapter;
import com.cp.ecommerce.adapter.persistence.customer.entity.CustomerEntityRepository;
import com.cp.ecommerce.domain.customer.port.outgoing.CheckCustomerOutPort;

import lombok.RequiredArgsConstructor;

/**
 * Implementation of {@link CheckCustomerOutPort}
 */
@PersistenceAdapter
@RequiredArgsConstructor
class CheckCustomerAdapter implements CheckCustomerOutPort {

    private final CustomerEntityRepository customerEntityRepository;

    @Override
    public boolean check(final String email) {

        return customerEntityRepository.existsByEmail(email);
    }

}
