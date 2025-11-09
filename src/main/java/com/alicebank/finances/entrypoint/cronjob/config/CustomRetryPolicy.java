package com.alicebank.finances.entrypoint.cronjob.config;

import com.alicebank.finances.core.exception.BusinessException;
import com.alicebank.finances.core.exception.ConflictException;
import org.springframework.classify.Classifier;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.stereotype.Component;

@Component
public class CustomRetryPolicy extends ExceptionClassifierRetryPolicy {

    private static final int RETRY_LIMIT = 3;

    public CustomRetryPolicy() {
        this.setExceptionClassifier((Classifier<Throwable, RetryPolicy>) throwable -> {
            if (throwable instanceof BusinessException || throwable instanceof ConflictException) {
                return new NeverRetryPolicy();
            } else {
                return new SimpleRetryPolicy(RETRY_LIMIT);
            }
        });
    }

}