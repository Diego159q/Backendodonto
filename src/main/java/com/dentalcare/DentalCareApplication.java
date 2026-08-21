package com.dentalcare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import java.util.TimeZone;

@SpringBootApplication
@EntityScan("com.dentalcare.entity")
@EnableJpaAuditing
@EnableScheduling
public class DentalCareApplication {

    @Value("${app.clinic.timezone:America/Lima}")
    private String clinicTimezone;

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(clinicTimezone));
    }

    public static void main(String[] args) {
        SpringApplication.run(DentalCareApplication.class, args);
    }
}
