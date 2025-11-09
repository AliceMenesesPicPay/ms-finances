package com.alicebank.finances.entrypoint.cronjob;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
public class ManualJobTrigger implements CommandLineRunner {
    private final JobLauncher jobLauncher;
    private final Job financesJob;

    public ManualJobTrigger(JobLauncher jobLauncher, Job financesJob) {
        this.jobLauncher = jobLauncher;
        this.financesJob = financesJob;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!shouldRun(args)) {
            log.info("ManualJobTrigger: Job NÃO disparado (flag ou parâmetro ausente).");
            return;
        }

        JobParameters jobParameters = buildParameters(args);

        log.info("ManualJobTrigger - Iniciando 'financesJob' com parâmetros: {}", jobParameters);

        var execution = jobLauncher.run(financesJob, jobParameters);

        log.info("ManualJobTrigger - Job finalizado com status: {}", execution.getStatus());
    }

    private boolean shouldRun(String[] args) {
        return Arrays.stream(args)
                .anyMatch(arg -> arg.equalsIgnoreCase("--runBatch=true")
                        || arg.equalsIgnoreCase("runBatch=true"));
    }

    private JobParameters buildParameters(String[] args) {
        JobParametersBuilder builder = new JobParametersBuilder()
                .addLong("manualRunId", System.currentTimeMillis());

        for (String arg : args) {
            if (arg.startsWith("--scheduledDate=")) {
                String date = arg.split("=")[1];
                builder.addString("scheduledDate", date);
            }
        }
        return builder.toJobParameters();
    }
}