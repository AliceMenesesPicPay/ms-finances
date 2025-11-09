package com.alicebank.finances;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@Configuration
@EnableRabbit
@ComponentScan(
		basePackages = "com.alicebank.finances",
		excludeFilters = {
				@ComponentScan.Filter(
						type = FilterType.REGEX,
						pattern = "com\\.alicebank\\.finances\\.entrypoint\\.cronjob\\..*"
				),
				@ComponentScan.Filter(
						type = FilterType.REGEX,
						pattern = "com\\.alicebank\\.finances\\.entrypoint\\.api\\..*"
				)
		}
)
public class TransactionWorkerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionWorkerApplication.class, args);
	}

}
