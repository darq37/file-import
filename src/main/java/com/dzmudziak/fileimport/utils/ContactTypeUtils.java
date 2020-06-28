package com.dzmudziak.fileimport.utils;

import com.dzmudziak.fileimport.domain.ContactType;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ContactTypeUtils {
    private static final String EMAIL_PATTERN = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
    private static final String PHONE_PATTERN = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{3}$";
    private static final String JABBER_PATTERN = "^[a-zA-Z]*$";

    public static ContactType getContactType(String contact) {
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

    private static Boolean isEmail(String contact) {
        Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
        Matcher emailMatcher = emailPattern.matcher(contact);
        return emailMatcher.matches();
    }

    private static Boolean isPhone(String contact) {
        Pattern phonePattern = Pattern.compile(PHONE_PATTERN);
        Matcher phoneMatcher = phonePattern.matcher(contact);
        return phoneMatcher.matches();
    }

    private static Boolean isJabber(String contact) {
        Pattern jabberPattern = Pattern.compile(JABBER_PATTERN);
        Matcher jabberMatcher = jabberPattern.matcher(contact);
        return jabberMatcher.matches();
    }
}
