package com.picpay.finances.dataprovider.integration.customer.client;

import com.picpay.finances.dataprovider.integration.customer.payload.CustomerResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-client", url = "${picpay.services.customer.url}")
@Headers({"Content-Type: application/json; charset=utf-8", "Accept: application/json; charset=utf-8"})
public interface CustomerClient {

    @GetMapping("/customers/{id}")
    CustomerResponse searchCustomerById(@PathVariable Long id);

}