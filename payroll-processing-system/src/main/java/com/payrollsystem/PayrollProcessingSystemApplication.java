package com.payrollsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main application class for payroll processing system.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.payrollsystem"})
public class PayrollProcessingSystemApplication {

    public static void main(final String[] args) {
        SpringApplication.run(PayrollProcessingSystemApplication.class, args);
    }

}
