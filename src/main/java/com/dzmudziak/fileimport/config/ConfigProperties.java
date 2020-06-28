package com.dzmudziak.fileimport.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class ConfigProperties {
    private String emailPattern;
    private String phonePattern;
    private String jabberPattern;

    public void setEmailPattern(String emailPattern) {
        this.emailPattern = emailPattern;
    }

    public void setPhonePattern(String phonePattern) {
        this.phonePattern = phonePattern;
    }

    public void setJabberPattern(String jabberPattern) {
        this.jabberPattern = jabberPattern;
    }

    public String getEmailPattern() {
        return emailPattern;
    }

    public String getPhonePattern() {
        return phonePattern;
    }

    public String getJabberPattern() {
        return jabberPattern;
    }
}
