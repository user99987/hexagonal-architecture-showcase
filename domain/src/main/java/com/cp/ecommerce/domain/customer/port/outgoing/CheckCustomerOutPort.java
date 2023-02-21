package com.cp.ecommerce.domain.customer.port.outgoing;

/**
 * Check customer exists outgoing port.
 */
public interface CheckCustomerOutPort {

    /**
     * Check customer exists by email.
     *
     * @param email customer email to be found.
     * @return customer exists flag.
     */
    boolean check(final String email);

}
