package com.picpay.finances.entrypoint.api.controller.payload.request;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CustomerIdRequest {

    @NotNull
    private Long customerId;

}
