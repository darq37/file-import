package com.dzmudziak.fileimport.batch;

import com.dzmudziak.fileimport.domain.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("ConstantConditions")
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
    @StepScope
    public FlatFileItemReader<Customer> csvReader(@Value("#{jobParameters['filePath']}") String filePath) {
        FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(Paths.get(filePath)));
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
    @StepScope
    public StaxEventItemReader<Customer> xmlReader(@Value("#{jobParameters['filePath']}") String filePath) {
        StaxEventItemReader<Customer> reader = new StaxEventItemReader<>();
        reader.setResource(new FileSystemResource(Paths.get(filePath)));
        reader.setFragmentRootElementName("person");
        reader.setUnmarshaller(customerMarshaller());
        return reader;
    }

    @Bean
    public XStreamMarshaller customerMarshaller() {
        Map<String, Class<?>> aliases = new HashMap<>();
        aliases.put("person", Customer.class);
        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setConverters(customerConverter);
        marshaller.setAliases(aliases);
        return marshaller;
    }


    @Bean
    public Job importXML() {
        return jobBuilderFactory.get("importCustomerJob")
                .incrementer(new RunIdIncrementer())
                .start(xmlFileStep())
                .build();
    }


    @Bean
    public Job importCSV() {
        return jobBuilderFactory.get("importCustomerJob")
                .incrementer(new RunIdIncrementer())
                .start(flatFileStep())
                .build();
    }

    @Bean
    public Step flatFileStep() {
        return stepBuilderFactory.get("flatFileStep")
                .<Customer, Customer>chunk(10)
                .reader(csvReader(null))
                .writer(customerItemWriter)
                .build();
    }

    @Bean
    public Step xmlFileStep() {
        return stepBuilderFactory.get("xmlFileStep")
                .<Customer, Customer>chunk(10)
                .reader(xmlReader(null))
                .writer(customerItemWriter)
                .build();
    }
}
