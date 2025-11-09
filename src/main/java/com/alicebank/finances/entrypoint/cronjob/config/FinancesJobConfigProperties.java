package com.alicebank.finances.entrypoint.cronjob.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "picpay.services.finances.job")
public record FinancesJobConfigProperties(int pageSize,
                                          int chunkSize) {


}
