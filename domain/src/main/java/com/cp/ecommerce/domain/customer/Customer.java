package com.cp.ecommerce.domain.customer;

import com.cp.ecommerce.adapter.common.constant.ValidationConstants;
import com.cp.ecommerce.adapter.common.validation.ValidDomainObject;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Class representing customer domain object.
 */
@Value
@EqualsAndHashCode(callSuper = false)
@Builder
public class Customer extends ValidDomainObject<Customer> {

    @NotNull(message = ValidationConstants.INVALID_ID)
    Long id;

    @Valid
    Contact contact;

    @Valid
    Address address;

    public static CustomerBuilder builder() {

        return new CustomerBuilder() {

            @Override
            public Customer build() {

                return super.build().validate();
            }
        };
    }

}
