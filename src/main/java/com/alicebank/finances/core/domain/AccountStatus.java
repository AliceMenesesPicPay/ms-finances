package com.alicebank.finances.core.domain;

public enum AccountStatus {

    ACTIVATED,
    CANCELED;

    public boolean isCanceled() {
        return this == CANCELED;
    }

}
