package com.alicebank.finances.entrypoint.common.config;

import com.alicebank.finances.entrypoint.cronjob.config.FinancesJobConfigProperties;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackages = "com.alicebank.finances")
@EntityScan(basePackages = "com.alicebank.finances.dataprovider.database.entity")
@EnableFeignClients(basePackages = "com.alicebank.finances.dataprovider.integration")
public class CommonConfig {
}
