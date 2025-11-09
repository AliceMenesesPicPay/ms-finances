package com.alicebank.finances.dataprovider.integration.customer.service;

import com.alicebank.finances.core.gateway.CustomerGateway;
import com.alicebank.finances.dataprovider.integration.customer.client.CustomerClient;
import com.alicebank.finances.dataprovider.integration.customer.exception.CustomerIntegrationException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerImpl implements CustomerGateway {

    private final CustomerClient customerClient;

    @Override
    public String getCustomerName(final Long customerId) {
        try {
            return "Alice " + customerId;
//            return customerClient.searchCustomerById(customerId).getName();
        } catch (FeignException e) {
            throw new CustomerIntegrationException(customerId, e);
        }
    }

}
