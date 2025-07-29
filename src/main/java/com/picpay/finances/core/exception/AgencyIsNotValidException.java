package com.picpay.finances.core.exception;

public class AgencyIsNotValidException extends BusinessException{

    public AgencyIsNotValidException(String agency) {
        super(String.format("Agency %s is not valid.", agency));
    }

}
