package com.bancoxyz.batch.config;

import com.bancoxyz.batch.config.BatchDataConfig.CuentaAnualInput;
import com.bancoxyz.batch.config.BatchDataConfig.CuentaAnualProcesada;
import com.bancoxyz.batch.listener.BatchJobListener;
import com.bancoxyz.batch.processor.CuentaAnualProcessor;
import com.bancoxyz.batch.writer.CuentaAnualWriter;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class EstadosAnualesJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final FlatFileItemReader<CuentaAnualInput> cuentasAnualesReader;
    private final CuentaAnualProcessor cuentaAnualProcessor;
    private final CuentaAnualWriter cuentaAnualWriter;
    private final BatchJobListener batchJobListener;

    public EstadosAnualesJobConfig(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            FlatFileItemReader<CuentaAnualInput> cuentasAnualesReader,
            CuentaAnualProcessor cuentaAnualProcessor,
            CuentaAnualWriter cuentaAnualWriter,
            BatchJobListener batchJobListener) {

        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.cuentasAnualesReader = cuentasAnualesReader;
        this.cuentaAnualProcessor = cuentaAnualProcessor;
        this.cuentaAnualWriter = cuentaAnualWriter;
        this.batchJobListener = batchJobListener;
    }

    @Bean
    public Job estadosAnualesJob() {

        return new JobBuilder(
                "estadosAnualesJob",
                jobRepository
        )
                .listener(batchJobListener)
                .start(estadosAnualesStep())
                .build();
    }

    @Bean
    public Step estadosAnualesStep() {

        return new StepBuilder(
                "estadosAnualesStep",
                jobRepository
        )
                .<CuentaAnualInput, CuentaAnualProcesada>chunk(5)
                .transactionManager(transactionManager)
                .reader(cuentasAnualesReader)
                .processor(cuentaAnualProcessor)
                .writer(cuentaAnualWriter)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(20)
                .build();
    }
}