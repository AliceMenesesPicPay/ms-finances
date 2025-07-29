package com.picpay.finances.entrypoint.api.controller.contract;

import com.picpay.finances.entrypoint.api.controller.payload.response.StatementResponse;
import org.springframework.data.domain.Pageable;

public interface StatementContract {

    StatementResponse searchByCustomerId(Long customerId, Pageable pageable);

}
