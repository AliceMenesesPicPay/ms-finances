package com.alicebank.finances;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
		basePackages = "com.alicebank.finances",
		excludeFilters = {
				@ComponentScan.Filter(
						type = FilterType.REGEX,
						pattern = "com\\.alicebank\\.finances\\.entrypoint\\.cronjob\\..*"
				),
				@ComponentScan.Filter(
						type = FilterType.REGEX,
						pattern = "com\\.alicebank\\.finances\\.entrypoint\\.worker\\..*"
				)
		}
)
public class FinancesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancesApiApplication.class, args);
	}

}
