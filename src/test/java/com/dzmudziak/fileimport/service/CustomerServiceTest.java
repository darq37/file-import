package com.dzmudziak.fileimport.service;

import com.dzmudziak.fileimport.domain.Customer;
import com.dzmudziak.fileimport.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(classes = CustomerService.class)
class CustomerServiceTest {
    @Autowired
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepository;

    @Test
    void shouldReturnCustomers() {
        List<Customer> expected = new LinkedList<>();
        expected.add(new Customer());
        doReturn(expected).when(customerRepository).findAll();

        List<Customer> customers = customerService.getCustomers();

        assertIterableEquals(expected, customers);
    }

    @Test
    void shouldReturnCustomer() {
        Customer customer = new Customer();
        Optional<Customer> expected = Optional.of(customer);
        doReturn(expected).when(customerRepository).findById(1L);

        Optional<Customer> actual = customerService.getCustomer(1L);

        assertEquals(expected, actual);
    }
}