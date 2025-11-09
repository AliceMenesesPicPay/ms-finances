package com.alicebank.finances.entrypoint.api.controller.handler;

import com.alicebank.finances.core.exception.BusinessException;
import com.alicebank.finances.core.exception.EntityNotFoundException;
import com.alicebank.finances.dataprovider.integration.customer.exception.CustomerIntegrationException;
import com.alicebank.finances.entrypoint.api.controller.handler.payload.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        var errorResponse = ErrorResponse.from(e.getMessage());

        return ResponseEntity.status(NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        var errorResponse = ErrorResponse.from(e.getMessage());

        return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(CustomerIntegrationException.class)
    public ResponseEntity<ErrorResponse> handleCustomerIntegrationException(CustomerIntegrationException e) {
        var errorResponse = ErrorResponse.from(e.getMessage());

        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}
