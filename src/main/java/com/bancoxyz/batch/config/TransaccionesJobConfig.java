package com.bancoxyz.batch.config;

import com.bancoxyz.batch.config.BatchDataConfig.TransaccionInput;
import com.bancoxyz.batch.config.BatchDataConfig.TransaccionProcesada;
import com.bancoxyz.batch.listener.BatchJobListener;
import com.bancoxyz.batch.processor.TransaccionProcessor;
import com.bancoxyz.batch.writer.TransaccionWriter;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TransaccionesJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final FlatFileItemReader<TransaccionInput> transaccionesReader;
    private final TransaccionProcessor transaccionProcessor;
    private final TransaccionWriter transaccionWriter;
    private final BatchJobListener batchJobListener;

    public TransaccionesJobConfig(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            FlatFileItemReader<TransaccionInput> transaccionesReader,
            TransaccionProcessor transaccionProcessor,
            TransaccionWriter transaccionWriter,
            BatchJobListener batchJobListener) {

        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.transaccionesReader = transaccionesReader;
        this.transaccionProcessor = transaccionProcessor;
        this.transaccionWriter = transaccionWriter;
        this.batchJobListener = batchJobListener;
    }

    @Bean
    public Job transaccionesJob() {

        return new JobBuilder(
                "transaccionesJob",
                jobRepository
        )
                .listener(batchJobListener)
                .start(transaccionesStep())
                .build();
    }

    @Bean
    public Step transaccionesStep() {

        return new StepBuilder(
                "transaccionesStep",
                jobRepository
        )
                .<TransaccionInput, TransaccionProcesada>chunk(5)
                .transactionManager(transactionManager)
                .reader(transaccionesReader)
                .processor(transaccionProcessor)
                .writer(transaccionWriter)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(20)
                .build();
    }
}
