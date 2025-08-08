package com.alicebank.finances.core.exception;

public class DigitIsNotValidException extends BusinessException{

    public DigitIsNotValidException(String number, String digit) {
        super(String.format("Account with number %s and digit %s is not valid.", number, digit));
    }

}
