package com.picpay.finances.cronjob.config;

import com.picpay.finances.core.domain.Transaction;
import com.picpay.finances.cronjob.processor.FinancesProcessor;
import com.picpay.finances.cronjob.reader.FinancesReader;
import com.picpay.finances.cronjob.writer.FinancesWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
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


    @Bean
    public Step financesStepConfig(final JobRepository jobRepository, final PlatformTransactionManager transactionManager, final FinancesReader financesReader,
                                   final FinancesProcessor financesProcessor, final FinancesWriter financesWriter) {
        return new StepBuilder("financesStepConfig", jobRepository)
                .<Transaction, Transaction>chunk(financesJobConfigProperties.chunkSize(), transactionManager)
                .reader(financesReader)
                .processor(financesProcessor)
                .writer(financesWriter)
                .build();
    }

    @Bean
    public Job financesJob(final JobRepository jobRepository, final @Qualifier("financesStepConfig") Step financesStepConfig) {
        log.info("[financesProcessStart]: process started.");
        return new JobBuilder("financesJob", jobRepository)
                .start(financesStepConfig)
                .build();
    }

}
