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
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    private final CustomerFieldSetMapper customerFieldSetMapper;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final CustomerItemWriter customerItemWriter;
    private final CustomerConverter customerConverter;

    @Autowired
    public BatchConfiguration(CustomerFieldSetMapper customerFieldSetMapper,
                              JobBuilderFactory jobBuilderFactory,
                              StepBuilderFactory stepBuilderFactory,
                              CustomerItemWriter customerItemWriter, CustomerConverter customerConverter) {
        this.customerFieldSetMapper = customerFieldSetMapper;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.customerItemWriter = customerItemWriter;
        this.customerConverter = customerConverter;
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
    public StaxEventItemReader<Customer> xmlReader() {
        StaxEventItemReader<Customer> reader = new StaxEventItemReader<>();
        reader.setResource(new ClassPathResource("dane-osoby.xml"));
        reader.setFragmentRootElementName("person");
        reader.setUnmarshaller(tradeMarshaller());
        return reader;
    }

    @Bean
    public XStreamMarshaller tradeMarshaller() {
        Map<String, Class<?>> aliases = new HashMap<>();
        aliases.put("person", Customer.class);
        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setConverters(customerConverter);
        marshaller.setAliases(aliases);
        return marshaller;
    }


    @Bean
    public Job importCustomerJob() {
        return jobBuilderFactory.get("importCustomerJob")
                .incrementer(new RunIdIncrementer())
                .start(xmlFileStep())
                .next(flatFileStep())
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

    @Bean
    public Step xmlFileStep() {
        return stepBuilderFactory.get("xmlFileStep")
                .<Customer, Customer>chunk(10)
                .reader(xmlReader())
                .writer(customerItemWriter)
                .build();
    }
}
