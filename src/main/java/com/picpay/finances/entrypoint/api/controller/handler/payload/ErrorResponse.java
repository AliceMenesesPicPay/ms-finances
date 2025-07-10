package com.picpay.finances.entrypoint.api.controller.handler.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private String message;
    private List<ErrorDetailResponse> errorDetails;

    public static ErrorResponse from(String message) {
        return ErrorResponse.builder()
                .message(message)
                .build();
    }

}
