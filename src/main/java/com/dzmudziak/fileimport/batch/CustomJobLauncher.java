package com.dzmudziak.fileimport.batch;

import com.dzmudziak.fileimport.config.ConfigProperties;
import org.apache.commons.io.FilenameUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CustomJobLauncher implements CommandLineRunner {
    private final JobLauncher jobLauncher;
    private final JobFactory jobFactory;
    private final ConfigProperties configProperties;

    @Autowired
    public CustomJobLauncher(JobLauncher jobLauncher, JobFactory jobFactory, ConfigProperties configProperties) {
        this.jobLauncher = jobLauncher;
        this.jobFactory = jobFactory;
        this.configProperties = configProperties;
    }

    @Override
    public void run(String... args) throws Exception {
        JobParametersBuilder builder = new JobParametersBuilder();
        JobParameters jobParameters = builder
                .addString("filePath", configProperties.getFilePath())
                .toJobParameters();
        String extension = FilenameUtils.getExtension(configProperties.getFilePath());
        Job job = jobFactory.getJob(extension);
        if (job == null) {
            System.out.println("No valid job selected - file extension not recognized.");
            return;
        }
        jobLauncher.run(job, jobParameters);
    }
}
