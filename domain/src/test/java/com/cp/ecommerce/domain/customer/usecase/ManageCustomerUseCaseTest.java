package com.cp.ecommerce.domain.customer.usecase;

import com.cp.ecommerce.domain.customer.port.outgoing.CheckCustomerOutPort;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link ManageCustomerUseCase}.
 */
@ExtendWith(MockitoExtension.class)
class ManageCustomerUseCaseTest {

    @Mock
    private transient CheckCustomerOutPort checkCustomerOutPort;

    @InjectMocks
    private transient ManageCustomerUseCase manageCustomerUseCase;

    @Test
    void shouldDelegateCustomerExistenceCheck() {

        when(checkCustomerOutPort.check("john.doe@test.com")).thenReturn(true);

        final boolean exists = manageCustomerUseCase.checkCustomerExists("john.doe@test.com");

        verify(checkCustomerOutPort).check("john.doe@test.com");
        assertTrue(exists);
    }

    @Test
    void shouldReturnFalseWhenCustomerDoesNotExist() {

        when(checkCustomerOutPort.check("missing@test.com")).thenReturn(false);

        final boolean exists = manageCustomerUseCase.checkCustomerExists("missing@test.com");

        verify(checkCustomerOutPort).check("missing@test.com");
        assertFalse(exists);
    }

}
