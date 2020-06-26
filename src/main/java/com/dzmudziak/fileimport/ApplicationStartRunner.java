package com.dzmudziak.fileimport;

import com.dzmudziak.fileimport.domain.Contact;
import com.dzmudziak.fileimport.domain.ContactType;
import com.dzmudziak.fileimport.domain.Customer;
import com.dzmudziak.fileimport.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartRunner implements ApplicationRunner {
    private final CustomerRepository customerRepository;

    @Autowired
    public ApplicationStartRunner(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        Customer customer = new Customer();
        customer.setName("Dominik");
        customer.setSurname("Żmudziak");
        customer.setAge(25);
        customer.setCity("Lublin");
        customer.getContacts().add(new Contact(customer, ContactType.UNKNOWN, "asdas"));
        customer.getContacts().add(new Contact(customer, ContactType.PHONE, "0123456789"));
        customer.getContacts().add(new Contact(customer, ContactType.EMAIL, "foo@bar.baz"));
        customer.getContacts().add(new Contact(customer, ContactType.JABBER, "JABBER"));
        customerRepository.save(customer);
    }
}
