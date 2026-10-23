package ru.academits.alekseev.phonebookspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PhoneBookSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(PhoneBookSpringBootApplication.class, args);
    }
}
