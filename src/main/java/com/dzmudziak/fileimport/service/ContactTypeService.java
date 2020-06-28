package com.dzmudziak.fileimport.service;

import com.dzmudziak.fileimport.config.ConfigProperties;
import com.dzmudziak.fileimport.domain.ContactType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ContactTypeService {
    private final ConfigProperties configProperties;

    @Autowired
    public ContactTypeService(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    public ContactType getContactType(String contact) {
        if (isEmail(contact)) {
            return ContactType.EMAIL;
        } else if (isPhone(contact)) {
            return ContactType.PHONE;
        } else if (isJabber(contact)) {
            return ContactType.JABBER;
        } else {
            return ContactType.UNKNOWN;
        }

    }

    private Boolean isEmail(String contact) {
        Pattern emailPattern = Pattern.compile(configProperties.getEmailPattern());
        Matcher emailMatcher = emailPattern.matcher(contact);
        return emailMatcher.matches();
    }

    private Boolean isPhone(String contact) {
        Pattern phonePattern = Pattern.compile(configProperties.getPhonePattern());
        Matcher phoneMatcher = phonePattern.matcher(contact);
        return phoneMatcher.matches();
    }

    private Boolean isJabber(String contact) {
        Pattern jabberPattern = Pattern.compile(configProperties.getJabberPattern());
        Matcher jabberMatcher = jabberPattern.matcher(contact);
        return jabberMatcher.matches();
    }
}
