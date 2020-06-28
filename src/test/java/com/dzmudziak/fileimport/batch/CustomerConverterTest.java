package com.dzmudziak.fileimport.batch;

import com.dzmudziak.fileimport.domain.Contact;
import com.dzmudziak.fileimport.domain.ContactType;
import com.dzmudziak.fileimport.domain.Customer;
import com.dzmudziak.fileimport.service.ContactTypeService;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(classes = {CustomerConverter.class})
class CustomerConverterTest {
    private static final String TEST_NAME = "TEST_NAME";
    private static final Integer AGE = 25;
    private static final String TEST_NUMBER = "123456789";
    private static final String TEST_SURNAME = "TEST_SURNAME";
    private static final String TEST_CITY = "TEST_CITY";
    @Mock
    private HierarchicalStreamReader reader;

    @Mock
    private UnmarshallingContext context;

    @MockBean
    private ContactTypeService contactTypeService;

    @Autowired
    private CustomerConverter customerConverter;

    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldAssignName() {
        doReturn("name").when(reader).getNodeName();
        doReturn(true).doReturn(false).when(reader).hasMoreChildren();
        doReturn(TEST_NAME).when(reader).getValue();

        Customer actual = (Customer) customerConverter.unmarshal(reader, context);

        assertEquals(TEST_NAME, actual.getName());
    }

    @Test
    void shouldAssignSurname() {
        doReturn("surname").when(reader).getNodeName();
        doReturn(true).doReturn(false).when(reader).hasMoreChildren();
        doReturn(TEST_SURNAME).when(reader).getValue();

        Customer actual = (Customer) customerConverter.unmarshal(reader, context);

        assertEquals(TEST_SURNAME, actual.getSurname());
    }

    @Test
    void shouldAssignCity() {
        doReturn("city").when(reader).getNodeName();
        doReturn(true).doReturn(false).when(reader).hasMoreChildren();
        doReturn(TEST_CITY).when(reader).getValue();

        Customer actual = (Customer) customerConverter.unmarshal(reader, context);

        assertEquals(TEST_CITY, actual.getCity());
    }

    @Test
    void shouldAssignNullAsAgeIfMissing() {
        doReturn("age").when(reader).getNodeName();
        doReturn(true).doReturn(false).when(reader).hasMoreChildren();
        doReturn(null).when(reader).getValue();

        Customer actual = (Customer) customerConverter.unmarshal(reader, context);

        assertNull(actual.getAge());
    }

    @Test
    void shouldAssignAge() {
        doReturn("age").when(reader).getNodeName();
        doReturn(true).doReturn(false).when(reader).hasMoreChildren();
        doReturn(String.valueOf(AGE)).when(reader).getValue();

        Customer actual = (Customer) customerConverter.unmarshal(reader, context);

        assertEquals(AGE, actual.getAge());
    }

    @Test
    void shouldAssignContact() {
        doReturn("contacts").when(reader).getNodeName();
        doReturn(true).doReturn(true).doReturn(false).when(reader).hasMoreChildren();
        doReturn(TEST_NUMBER).when(reader).getValue();
        doReturn(ContactType.PHONE).when(contactTypeService).getContactType(TEST_NUMBER);

        Customer actual = (Customer) customerConverter.unmarshal(reader, context);

        Customer expected = new Customer();
        Contact contact = new Contact();
        contact.setType(ContactType.PHONE);
        contact.setCustomer(expected);
        contact.setContact(TEST_NUMBER);
        expected.getContacts().add(contact);
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}