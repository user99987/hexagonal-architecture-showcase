package com.cp.ecommerce.domain.customer;

import com.cp.ecommerce.adapter.common.exception.DomainObjectValidationException;
import com.cp.ecommerce.domain.support.TestDomainObjectFactory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link Customer}.
 */
class CustomerTest {

    @Test
    void shouldPassValidationForValidCustomer() {

        final Customer customer = TestDomainObjectFactory.validCustomer();

        assertDoesNotThrow(customer::assertValidationsEmpty);
    }

    @Test
    void shouldFailValidationForMissingId() {

        final Customer customer = Customer.builder()
                .contact(TestDomainObjectFactory.validContact())
                .address(TestDomainObjectFactory.validAddress())
                .build();

        assertThrows(DomainObjectValidationException.class, customer::assertValidationsEmpty);
    }

}
