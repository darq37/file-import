package com.dzmudziak.fileimport.service;

import com.dzmudziak.fileimport.config.ConfigProperties;
import com.dzmudziak.fileimport.domain.ContactType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {ContactTypeService.class, ConfigProperties.class})
class ContactTypeServiceTest {
    private static final String TEST_EMAIL = "mail@gmail.com";
    private static final String TEST_PHONE = "606-666-123";
    private static final String TEST_JABBER = "jibberjabber";
    private static final String TEST_UNKNOWN = "ssd 22c al.";
    @Autowired
    private ContactTypeService contactTypeService;

    @Test
    void shouldMatchEmail() {
        assertEquals(ContactType.EMAIL, contactTypeService.getContactType(TEST_EMAIL));
    }

    @Test
    void shouldMatchPhone() {
        assertEquals(ContactType.PHONE, contactTypeService.getContactType(TEST_PHONE));
    }

    @Test
    void shouldMatchJabber() {
        assertEquals(ContactType.JABBER, contactTypeService.getContactType(TEST_JABBER));
    }

    @Test
    void shouldMatchUnknown() {
        assertEquals(ContactType.UNKNOWN, contactTypeService.getContactType(TEST_UNKNOWN));
    }

    @Test
    void shouldMatchUnknownForNull() {
        assertEquals(ContactType.UNKNOWN, contactTypeService.getContactType(null));
    }
}