package com.koushik.expansetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExpanseTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpanseTrackerApplication.class, args);
    }

}
