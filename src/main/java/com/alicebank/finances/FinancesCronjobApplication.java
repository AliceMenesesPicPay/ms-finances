package com.alicebank.finances;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@EnableBatchProcessing
@ComponentScan(
        basePackages = "com.alicebank.finances",
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.REGEX,
                        pattern = "com\\.alicebank\\.finances\\.entrypoint\\.api\\..*"
                ),
                @ComponentScan.Filter(
                        type = FilterType.REGEX,
                        pattern = "com\\.alicebank\\.finances\\.entrypoint\\.worker\\..*"
                )
        }
)
public class FinancesCronjobApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(FinancesCronjobApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }

}
