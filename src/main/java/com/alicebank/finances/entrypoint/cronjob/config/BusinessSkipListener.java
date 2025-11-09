package com.alicebank.finances.entrypoint.cronjob.config;

import com.alicebank.finances.core.domain.TransactionScheduled;
import com.alicebank.finances.core.gateway.TransactionScheduledGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BusinessSkipListener implements SkipListener<TransactionScheduled, TransactionScheduled> {

    private final TransactionScheduledGateway transactionScheduledGateway;

    @Override
    public void onSkipInWrite(TransactionScheduled transactionScheduled, Throwable t) {
        log.error("SKIP WRITE - TransactionScheduled ID: {}. Reason: {}", transactionScheduled.getId(), t.getMessage());

        transactionScheduled.cancel();
        transactionScheduledGateway.save(transactionScheduled);

    }

}
