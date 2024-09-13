package com.payrollsystem.service;

import com.payrollsystem.dto.*;
import com.payrollsystem.response.PayrollProcessingResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Service interface for payroll processing operations.
 */
public interface PayrollService {

    /**
     * Processes the uploaded payroll file and records the events.
     *
     * @param file the multipart file to be processed
     * @return a response indicating the success or failure of the operation
     */
    PayrollProcessingResponse processFile(MultipartFile file);

    /**
     * Generates a monthly salary report summarizing salaries by month.
     *
     * @return a list of monthly report data transfer objects
     * @throws IOException if there's an error reading from the file
     */
    List<MonthlyReportDTO> generateMonthlySalaryReport() throws IOException;

    /**
     * Generates a yearly financial report detailing financial events by employee and date.
     *
     * @return a list of yearly financial report data transfer objects
     * @throws IOException if there's an error reading from the file
     */
    List<YearlyFinancialReportDTO> generateYearlyFinancialReport() throws IOException;

    /**
     * Retrieves the total number of unique employees based on employee IDs.
     *
     * @return the total count of unique employees
     * @throws IOException if there's an error reading from the file
     */
    Integer getTotalEmployees() throws IOException;

    /**
     * Retrieves events for employees filtered by a specific event type and grouped by month.
     *
     * @param event the type of event to filter by
     * @return a map of month strings to lists of event data transfer objects
     * @throws IOException if there's an error reading from the file
     */
    Map<String, List<EventDTO>> getEmployeeEventsByMonth(String event) throws IOException;

    /**
     * Generates a comprehensive financial report for each employee.
     *
     * @return a list of employee financial report data transfer objects
     * @throws IOException if there's an error reading from the file
     */
    List<EmployeeFinancialReportDTO> getEmployeeFinancialReport() throws IOException;

    /**
     * Summarizes monthly amounts for payroll-related events.
     *
     * @return a list of monthly amount report data transfer objects
     * @throws IOException if there's an error reading from the file
     */
    List<MonthlyAmountReportDTO> getMonthlyAmountReport() throws IOException;
}