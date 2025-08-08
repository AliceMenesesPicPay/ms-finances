package com.picpay.finances.dataprovider.integration.customer.exception;

public class CustomerIntegrationException extends RuntimeException {

    public CustomerIntegrationException(Long customerId, Throwable throwable) {
        super(String.format("An error occurred while trying to get the customer name with ID %d", customerId), throwable);
    }

}
