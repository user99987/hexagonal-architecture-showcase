package com.cp.ecommerce.adapter.persistence.customer;

import com.cp.ecommerce.adapter.persistence.customer.entity.CustomerEntityRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static com.cp.ecommerce.adapter.common.utils.CustomerBuilder.TEST_EMAIL;

/**
 * Test class for {@link CheckCustomerAdapter}.
 */
@ExtendWith(MockitoExtension.class)
class CheckCustomerAdapterTest {

    @InjectMocks
    private transient CheckCustomerAdapter checkCustomerAdapter;

    @Mock
    private transient CustomerEntityRepository customerEntityRepository;

    @Test
    void shouldNotFindCustomer() {

        final boolean found = checkCustomerAdapter.check(TEST_EMAIL);
        assertFalse(found);
    }

    @Test
    void shouldFindCustomer() {

        doReturn(true).when(customerEntityRepository).existsByEmail(TEST_EMAIL);
        final boolean result = checkCustomerAdapter.check(TEST_EMAIL);
        verify(customerEntityRepository, times(1)).existsByEmail(TEST_EMAIL);
        assertTrue(result);
    }
}
