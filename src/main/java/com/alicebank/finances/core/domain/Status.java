package com.alicebank.finances.core.domain;

public enum Status {

    ACTIVATED,
    CANCELED;

    public boolean isCanceled() {
        return this == CANCELED;
    }

}
