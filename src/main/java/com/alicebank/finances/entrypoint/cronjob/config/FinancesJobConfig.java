package com.alicebank.finances.entrypoint.cronjob.config;

import com.alicebank.finances.core.domain.Transaction;
import com.alicebank.finances.core.domain.TransactionScheduled;
import com.alicebank.finances.core.exception.BusinessException;
import com.alicebank.finances.core.exception.ConflictException;
import com.alicebank.finances.entrypoint.cronjob.reader.FinancesReader;
import com.alicebank.finances.entrypoint.cronjob.writer.FinancesWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class FinancesJobConfig {

    private final FinancesJobConfigProperties financesJobConfigProperties;
    private final BusinessSkipListener businessSkipListener;
    private final CustomRetryPolicy customRetryPolicy;

    @Bean
    public Step financesStepConfig(final JobRepository jobRepository, final PlatformTransactionManager transactionManager, final FinancesReader financesReader,
                                   final FinancesWriter financesWriter) {
        return new StepBuilder("financesStepConfig", jobRepository)
                .<TransactionScheduled, TransactionScheduled>chunk(financesJobConfigProperties.chunkSize(), transactionManager)
                .reader(financesReader)
                .writer(financesWriter)
                .faultTolerant()
                .skip(BusinessException.class)
                .skip(ConflictException.class)
                .skipLimit(Integer.MAX_VALUE)
                .listener(businessSkipListener)
                .retry(Exception.class)
                .retryPolicy(customRetryPolicy)
                .build();
    }

    @Bean
    public Job financesJob(final JobRepository jobRepository, final @Qualifier("financesStepConfig") Step financesStepConfig) {
        log.info("[financesProcessStart]: process started.");
        return new JobBuilder("financesJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(financesStepConfig)
                .build();
    }

}
