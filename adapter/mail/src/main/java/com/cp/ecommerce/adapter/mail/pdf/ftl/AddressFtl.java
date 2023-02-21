package com.cp.ecommerce.adapter.mail.pdf.ftl;

import java.io.Serializable;

import com.cp.ecommerce.domain.customer.Address;

import lombok.Builder;
import lombok.Getter;

/**
 * FTL mapper used for mapping {@link Address} and {@link Address} objects into theirs FTL counterparts.
 */
@Getter
@Builder
public class AddressFtl implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String countryCode;

    private final String postalCode;

    private final String city;

    private final String street;

    public static AddressFtl of(final Address address) {

        if (address == null) {
            return null;
        }

        return AddressFtl.builder()
                .street(address.getStreet())
                .countryCode(address.getCountryCode())
                .postalCode(address.getPostalCode())
                .city(address.getCity())
                .build();
    }

}
