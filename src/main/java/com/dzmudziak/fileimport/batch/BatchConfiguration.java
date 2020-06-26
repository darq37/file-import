package com.dzmudziak.fileimport.batch;

import com.dzmudziak.fileimport.domain.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    private final CustomerFieldSetMapper customerFieldSetMapper;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final CustomerItemWriter customerItemWriter;


    @Autowired
    public BatchConfiguration(CustomerFieldSetMapper customerFieldSetMapper,
                              JobBuilderFactory jobBuilderFactory,
                              StepBuilderFactory stepBuilderFactory,
                              CustomerItemWriter customerItemWriter) {
        this.customerFieldSetMapper = customerFieldSetMapper;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.customerItemWriter = customerItemWriter;
    }

    @Bean
    public FlatFileItemReader<Customer> csvReader() {
        FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("dane-osoby.txt"));
        reader.setEncoding(StandardCharsets.UTF_8.name());
        reader.setLineMapper(new DefaultLineMapper<>() {
            {
                setLineTokenizer(s -> new DefaultFieldSet(Objects.requireNonNull(s).split(",")));
                setFieldSetMapper(customerFieldSetMapper);
            }
        });
        return reader;
    }

    @Bean
    public Job importCustomerJob() {
        return jobBuilderFactory.get("importCustomerJob")
                .incrementer(new RunIdIncrementer())
                .flow(flatFileStep())
                .end()
                .build();
    }

    @Bean
    public Step flatFileStep() {
        return stepBuilderFactory.get("flatFileStep")
                .<Customer, Customer>chunk(10)
                .reader(csvReader())
                .writer(customerItemWriter)
                .build();
    }
}
