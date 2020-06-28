package com.dzmudziak.fileimport.batch;

import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class JobFactory {
    private static final String CSV = "CSV";
    private static final String TXT = "TXT";
    private static final String XML = "XML";

    private final Job csvJob;
    private final Job xmlJob;

    @Autowired
    public JobFactory(@Qualifier("importCSV") Job csvJob, @Qualifier("importXML") Job xmlJob) {
        this.csvJob = csvJob;
        this.xmlJob = xmlJob;
    }

    Job getJob(String fileType) {
        if (CSV.equalsIgnoreCase(fileType) || TXT.equalsIgnoreCase(fileType)) {
            return csvJob;
        } else if (XML.equalsIgnoreCase(fileType)) {
            return xmlJob;
        } else {
            return null;
        }
    }
}