package com.dzmudziak.fileimport.batch;

import com.dzmudziak.fileimport.domain.Contact;
import com.dzmudziak.fileimport.domain.Customer;
import com.dzmudziak.fileimport.service.ContactTypeService;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CustomerConverter implements Converter {
    private final ContactTypeService contactTypeService;

    @Autowired
    public CustomerConverter(ContactTypeService contactTypeService) {
        this.contactTypeService = contactTypeService;
    }


    @Override
    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter, MarshallingContext marshallingContext) {
        // noop
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Customer customer = new Customer();
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            String nodeName = reader.getNodeName();
            if ("name".equalsIgnoreCase(nodeName)) {
                customer.setName(reader.getValue());
            } else if ("surname".equalsIgnoreCase(nodeName)) {
                customer.setSurname(reader.getValue());
            } else if ("age".equalsIgnoreCase(nodeName)) {
                customer.setAge(Integer.valueOf(reader.getValue()));
            } else if ("city".equalsIgnoreCase(nodeName)) {
                customer.setCity(reader.getValue());
            } else if ("contacts".equalsIgnoreCase(reader.getNodeName())) {
                while (reader.hasMoreChildren()) {
                    reader.moveDown();
                    Contact contact = new Contact();
                    contact.setType(contactTypeService.getContactType(reader.getValue()));
                    contact.setCustomer(customer);
                    contact.setContact(reader.getValue());
                    customer.getContacts().add(contact);
                    reader.moveUp();
                }
            }
            reader.moveUp();
        }
        return customer;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return Customer.class.isAssignableFrom(aClass);
    }
}
