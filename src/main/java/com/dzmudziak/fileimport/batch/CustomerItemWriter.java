package com.dzmudziak.fileimport.batch;

import com.dzmudziak.fileimport.domain.Customer;
import com.dzmudziak.fileimport.repository.CustomerRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerItemWriter implements ItemWriter<Customer> {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerItemWriter(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void write(List<? extends Customer> list) {
        customerRepository.saveAll(list);
    }
}