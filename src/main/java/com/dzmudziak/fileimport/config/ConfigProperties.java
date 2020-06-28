package com.dzmudziak.fileimport.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class ConfigProperties {
    private String emailPattern = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
    private String phonePattern = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{3}$";
    private String jabberPattern = "^[a-zA-Z]*$";
    private String filePath;

    public String getEmailPattern() {
        return emailPattern;
    }

    public void setEmailPattern(String emailPattern) {
        this.emailPattern = emailPattern;
    }

    public String getPhonePattern() {
        return phonePattern;
    }

    public void setPhonePattern(String phonePattern) {
        this.phonePattern = phonePattern;
    }

    public String getJabberPattern() {
        return jabberPattern;
    }

    public void setJabberPattern(String jabberPattern) {
        this.jabberPattern = jabberPattern;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
