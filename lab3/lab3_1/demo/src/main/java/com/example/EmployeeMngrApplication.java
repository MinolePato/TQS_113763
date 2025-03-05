package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Sample for a typical Spring Boot application.
 * Offers a REST API with basic endpoints to manage a list of Employees
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.example")
public class EmployeeMngrApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeMngrApplication.class, args);
    }

}

