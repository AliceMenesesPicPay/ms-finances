package com.alicebank.finances.entrypoint.api.controller.handler;

import com.alicebank.finances.core.exception.BusinessException;
import com.alicebank.finances.core.exception.EntityNotFoundException;
import com.alicebank.finances.entrypoint.api.controller.handler.payload.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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

}
