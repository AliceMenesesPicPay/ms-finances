package com.picpay.finances.cronjob.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "picpay.finances.job")
public record FinancesJobConfigProperties(int pageSize,
                                          int chunkSize) {


}
