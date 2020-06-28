package com.dzmudziak.fileimport.batch;

import com.dzmudziak.fileimport.domain.Contact;
import com.dzmudziak.fileimport.domain.ContactType;
import com.dzmudziak.fileimport.domain.Customer;
import com.dzmudziak.fileimport.service.ContactTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {CustomerFieldSetMapper.class})
class CustomerFieldSetMapperTest {
    private static final int TEST_AGE = 25;
    private static final String TEST_CONTACT = "TEST_CONTACT";
    private static final String TEST_CITY = "TEST_CITY";
    private static final String TEST_SURNAME = "TEST_SURNAME";
    private static final String TEST_NAME = "TEST_NAME";
    private FieldSet fieldSet;

    @MockBean
    private ContactTypeService contactTypeService;

    @Autowired
    private CustomerFieldSetMapper mapper;

    @BeforeEach
    void setUp() {
        fieldSet = mock(FieldSet.class);
    }

    @Test
    void shouldMapValidFieldSetToCustomer() {
        doReturn(TEST_NAME).when(fieldSet).readString(0);
        doReturn(TEST_SURNAME).when(fieldSet).readString(1);
        doReturn(TEST_AGE).when(fieldSet).readInt(2);
        doReturn(TEST_CITY).when(fieldSet).readString(3);
        doReturn(5).when(fieldSet).getFieldCount();
        doReturn(TEST_CONTACT).when(fieldSet).readString(4);
        doReturn(ContactType.UNKNOWN).when(contactTypeService).getContactType(anyString());

        Customer actual = mapper.mapFieldSet(fieldSet);

        Customer expected = new Customer();
        expected.setName(TEST_NAME);
        expected.setSurname(TEST_SURNAME);
        expected.setAge(TEST_AGE);
        expected.setCity(TEST_CITY);
        Contact expectedContact = new Contact();
        expectedContact.setType(ContactType.UNKNOWN);
        expectedContact.setCustomer(expected);
        expectedContact.setContact(TEST_CONTACT);
        expected.getContacts().add(expectedContact);
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void shouldReturnNoContacts() {
        doReturn(4).when(fieldSet).getFieldCount();

        Customer actual = mapper.mapFieldSet(fieldSet);

        assertEquals(0, actual.getContacts().size());
    }

    @Test
    void shouldReturnNullAge() {
        doThrow(NumberFormatException.class).when(fieldSet).readInt(2);

        Customer actual = mapper.mapFieldSet(fieldSet);

        assertNull(actual.getAge());
    }


}