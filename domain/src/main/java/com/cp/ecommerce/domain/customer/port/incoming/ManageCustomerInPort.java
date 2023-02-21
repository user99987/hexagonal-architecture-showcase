package com.cp.ecommerce.domain.customer.port.incoming;

/**
 * Manage customer incoming port.
 */
public interface ManageCustomerInPort {

    /**
     * This method is meant to trigger all steps to save customer data to the database.
     *
     * @param email email address of customer.
     * @return customer exists flag.
     */
    boolean checkCustomerExists(final String email);
}
