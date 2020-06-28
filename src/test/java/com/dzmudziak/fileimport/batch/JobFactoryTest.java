package com.dzmudziak.fileimport.batch;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SpringBootTest(classes = {JobFactory.class, Job.class})
class JobFactoryTest {
    @Autowired
    private JobFactory jobFactory;

    @Qualifier("importCSV")
    @MockBean
    private Job csvJob;

    @Qualifier("importXML")
    @MockBean
    private Job xmlJob;


    @Test
    void shouldReturnXmlJob() {
        Job job = jobFactory.getJob("XML");
        assertEquals(xmlJob, job);
    }

    @Test
    void shouldReturnCsvJobForCSV() {
        Job job = jobFactory.getJob("CSV");
        assertEquals(csvJob, job);
    }

    @Test
    void shouldReturnCsvJobForTXT() {
        Job job = jobFactory.getJob("TXT");
        assertEquals(csvJob, job);
    }

    @Test
    void shouldReturnNullForUnknownType() {
        Job job = jobFactory.getJob("FOO");
        assertNull(job);
    }

}