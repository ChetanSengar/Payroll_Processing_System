package com.payrollsystem.controller;

import com.payrollsystem.dto.*;
import com.payrollsystem.response.PayrollProcessingResponse;
import com.payrollsystem.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.payrollsystem.exception.CustomExceptionEnum.NO_FILE_FOUND;

/**
 * Controller for handling payroll-related requests.
 */
@RestController
@RequestMapping("/api/payroll")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;

    /**
     * Uploads and processes a payroll file.
     *
     * @param files the files to be uploaded
     * @return a response entity with the processing results
     */
    @PostMapping("/upload")
    public ResponseEntity<PayrollProcessingResponse> uploadFile(@RequestParam("files") final MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return new ResponseEntity<>(new PayrollProcessingResponse(false,
                    List.of(NO_FILE_FOUND.getExceptionDescription()), true), HttpStatus.BAD_REQUEST);
        }
        try {
            final List<EventDTO> allEvents = new ArrayList<>();
            final List<String> errorDescriptions = new ArrayList<>();
            boolean allSuccess = true;

            for (final MultipartFile file : files) {
                final PayrollProcessingResponse res = payrollService.processFile(file);
                if (res.isSuccess()) {
                    if (res.getEvents() != null) {
                        allEvents.addAll(res.getEvents());
                    }
                } else {
                    allSuccess = false;
                    if (res.getErrorDescriptions() != null) {
                        errorDescriptions.addAll(res.getErrorDescriptions());
                    }
                }
                System.out.println("File processed: " + file.getOriginalFilename() + ", Success: " + res.isSuccess());
            }
            System.out.println("Final Success Status: " + allSuccess);
            final PayrollProcessingResponse finalResponse = allSuccess ?
                    new PayrollProcessingResponse(true, allEvents) :
                    new PayrollProcessingResponse(false, errorDescriptions, true);

            return new ResponseEntity<>(finalResponse, allSuccess ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
        } catch (final Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves monthly salary reports.
     *
     * @return a list of monthly salary reports
     */
    @GetMapping("/monthly-salary")
    public ResponseEntity<List<MonthlyReportDTO>> getMonthlySalaryReport() {
        try {
            final List<MonthlyReportDTO> reports = payrollService.generateMonthlySalaryReport();
            return new ResponseEntity<>(reports, HttpStatus.OK);
        } catch (final Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves yearly financial reports.
     *
     * @return a list of yearly financial reports
     */
    @GetMapping("/yearly-financial")
    public ResponseEntity<List<YearlyFinancialReportDTO>> getYearlyFinancialReport() {
        try {
            final List<YearlyFinancialReportDTO> reports = payrollService.generateYearlyFinancialReport();
            return new ResponseEntity<>(reports, HttpStatus.OK);
        } catch (final Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets the total number of employees.
     *
     * @return the total number of employees
     */
    @GetMapping("/total-employees")
    public ResponseEntity<Integer> getTotalEmployees() {
        try {
            final int totalEmployees = payrollService.getTotalEmployees();
            return new ResponseEntity<>(totalEmployees, HttpStatus.OK);
        } catch (final Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves monthly joining events.
     *
     * @return a map of monthly joining events
     */
    @GetMapping("/monthly-joins")
    public ResponseEntity<Map<String, List<EventDTO>>> getMonthlyJoins() {
        try {
            final Map<String, List<EventDTO>> monthlyJoins = payrollService.getEmployeeEventsByMonth("ONBOARD");
            return new ResponseEntity<>(monthlyJoins, HttpStatus.OK);
        } catch (final Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves monthly exit events.
     *
     * @return a map of monthly exit events
     */
    @GetMapping("/monthly-exits")
    public ResponseEntity<Map<String, List<EventDTO>>> getMonthlyExits() {
        try {
            final Map<String, List<EventDTO>> monthlyExits = payrollService.getEmployeeEventsByMonth("EXIT");
            return new ResponseEntity<>(monthlyExits, HttpStatus.OK);
        } catch (final Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves financial reports for employees.
     *
     * @return a list of employee financial reports
     */
    @GetMapping("/employee-financial-report")
    public ResponseEntity<List<EmployeeFinancialReportDTO>> getEmployeeFinancialReport() {
        try {
            final List<EmployeeFinancialReportDTO> reports = payrollService.getEmployeeFinancialReport();
            return new ResponseEntity<>(reports, HttpStatus.OK);
        } catch (final Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves monthly amount reports.
     *
     * @return a list of monthly amount reports
     */
    @GetMapping("/monthly-amount")
    public ResponseEntity<List<MonthlyAmountReportDTO>> getMonthlyAmountReport() {
        try {
            final List<MonthlyAmountReportDTO> reports = payrollService.getMonthlyAmountReport();
            return new ResponseEntity<>(reports, HttpStatus.OK);
        } catch (final Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
