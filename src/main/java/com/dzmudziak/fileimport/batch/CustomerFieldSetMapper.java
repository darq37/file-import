package com.dzmudziak.fileimport.batch;

import com.dzmudziak.fileimport.domain.Contact;
import com.dzmudziak.fileimport.domain.Customer;
import com.dzmudziak.fileimport.service.ContactTypeService;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CustomerFieldSetMapper implements FieldSetMapper<Customer> {
    private final ContactTypeService contactTypeService;

    @Autowired
    public CustomerFieldSetMapper(ContactTypeService contactTypeService) {
        this.contactTypeService = contactTypeService;
    }


    @Override
    public Customer mapFieldSet(FieldSet fieldSet) {
        Customer customer = new Customer();
        customer.setName(fieldSet.readString(0));
        customer.setSurname(fieldSet.readString(1));
        try {
            customer.setAge(fieldSet.readInt(2));
        } catch (NumberFormatException nfe) {
            customer.setAge(null);
        }
        customer.setCity(fieldSet.readString(3));
        for (int i = 4; i < fieldSet.getFieldCount(); i++) {
            Contact contact = new Contact();
            contact.setCustomer(customer);
            contact.setContact(fieldSet.readString(i));
            contact.setType(contactTypeService.getContactType(fieldSet.readString(i)));
            customer.getContacts().add(contact);
        }
        return customer;
    }

}
