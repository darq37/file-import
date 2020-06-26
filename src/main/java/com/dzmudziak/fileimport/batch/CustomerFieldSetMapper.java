package com.dzmudziak.fileimport.batch;

import com.dzmudziak.fileimport.domain.Contact;
import com.dzmudziak.fileimport.domain.ContactType;
import com.dzmudziak.fileimport.domain.Customer;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;

@Component
public class CustomerFieldSetMapper implements FieldSetMapper<Customer> {
    @Override
    public Customer mapFieldSet(FieldSet fieldSet) {
        Customer customer = new Customer();
        customer.setName(fieldSet.readString(0));
        customer.setSurname(fieldSet.readString(1));
        try {
            customer.setAge(fieldSet.readInt(2));
        } catch (NumberFormatException nfe) {
            customer.setAge(0);
        }
        customer.setCity(fieldSet.readString(3));
        for (int i = 4; i < fieldSet.getFieldCount(); i++) {
            Contact contact = new Contact();
            contact.setCustomer(customer);
            contact.setContact(fieldSet.readString(i));
            contact.setType(ContactType.UNKNOWN);
            customer.getContacts().add(contact);
        }
        return customer;
    }
}
