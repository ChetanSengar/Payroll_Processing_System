package com.payrollsystem;

import com.payrollsystem.controller.PayrollController;
import com.payrollsystem.dto.*;
import com.payrollsystem.response.PayrollProcessingResponse;
import com.payrollsystem.service.PayrollService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = PayrollController.class)
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
public class PayrollControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PayrollService payrollService;

    @Test
    public void testFileUpload() throws Exception {
        final MockMultipartFile file = new MockMultipartFile(
                "files",
                "employees.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "sequenceNo,empId,firstName,lastName,designation,event,value,eventDate,notes\n1,100,John,Doe,Engineer,Update,5000,2021-04-01,Salary Update".getBytes()
        );

        when(payrollService.processFile(any())).thenReturn(new PayrollProcessingResponse(true, List.of()));

        mockMvc.perform(multipart("/api/payroll/upload")
                        .file(file)
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.events").isEmpty());  // Ensure that the events list is empty
    }

    @Test
    public void testFileUpload_Failure() throws Exception {
        final MockMultipartFile file = new MockMultipartFile(
                "files",
                "employees.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "corrupted data".getBytes()
        );
        when(payrollService.processFile(any())).thenReturn(new PayrollProcessingResponse(false, List.of("Invalid file format"), false));

        mockMvc.perform(multipart("/api/payroll/upload")
                        .file(file)
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorDescriptions[0]").value("Invalid file format"));
    }

    @Test
    public void testFileUpload_InternalServerError() throws Exception {
        final MockMultipartFile file = new MockMultipartFile(
                "files",
                "employees.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "sequenceNo,empId,firstName,lastName,designation,event,value,eventDate,notes\n1,100,John,Doe,Engineer,Update,5000,2021-04-01,Salary Update".getBytes()
        );

        when(payrollService.processFile(any())).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(multipart("/api/payroll/upload")
                        .file(file)
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testGetMonthlySalaryReport() throws Exception {
        final List<MonthlyReportDTO> mockReports = List.of(new MonthlyReportDTO("January", 5000.0, 10));
        when(payrollService.generateMonthlySalaryReport()).thenReturn(mockReports);

        mockMvc.perform(get("/api/payroll/monthly-salary")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].month").value("January"))
                .andExpect(jsonPath("$[0].totalSalary").value(5000.0))
                .andExpect(jsonPath("$[0].totalEmployees").value(10));
    }

    @Test
    public void testGetYearlyFinancialReport() throws Exception {
        final List<YearlyFinancialReportDTO> mockReports = List.of(new YearlyFinancialReportDTO("Event1", "emp101", new Date(), 5000.0));
        when(payrollService.generateYearlyFinancialReport()).thenReturn(mockReports);

        mockMvc.perform(get("/api/payroll/yearly-financial")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].event").value("Event1"))
                .andExpect(jsonPath("$[0].empId").value("emp101"))
                .andExpect(jsonPath("$[0].eventValue").value(5000.0));
    }

    @Test
    public void testGetTotalEmployees() throws Exception {
        when(payrollService.getTotalEmployees()).thenReturn(100);

        mockMvc.perform(get("/api/payroll/total-employees")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(100));
    }

    @Test
    public void testGetMonthlyJoins() throws Exception {
        final Map<String, List<EventDTO>> mockJoins = Map.of(
                "January", List.of(new EventDTO("2", "emp102", "John", "Doe", "Manager", "ONBOARD", 3000.0, LocalDate.now(), "Joined the team"))
        );
        when(payrollService.getEmployeeEventsByMonth("ONBOARD")).thenReturn(mockJoins);

        mockMvc.perform(get("/api/payroll/monthly-joins")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.January[0].empId").value("emp102"))
                .andExpect(jsonPath("$.January[0].firstName").value("John"));
    }

    @Test
    public void testGetMonthlyExits() throws Exception {
        final Map<String, List<EventDTO>> mockExits = Map.of(
                "February", List.of(new EventDTO("1", "emp103", "Jane", "Doe", "Engineer", "EXIT", 0.0, LocalDate.now(), "Left the company"))
        );
        when(payrollService.getEmployeeEventsByMonth("EXIT")).thenReturn(mockExits);

        mockMvc.perform(get("/api/payroll/monthly-exits")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.February[0].empId").value("emp103"))
                .andExpect(jsonPath("$.February[0].lastName").value("Doe"));
    }

    @Test
    public void testGetEmployeeFinancialReport() throws Exception {
        final List<EmployeeFinancialReportDTO> mockReports = List.of(new EmployeeFinancialReportDTO("emp104", "Alice", "Smith", 8000.0));
        when(payrollService.getEmployeeFinancialReport()).thenReturn(mockReports);

        mockMvc.perform(get("/api/payroll/employee-financial-report")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].empId").value("emp104"))
                .andExpect(jsonPath("$[0].totalAmountPaid").value(8000.0));
    }

    @Test
    public void testGetMonthlyAmountReport() throws Exception {
        final List<MonthlyAmountReportDTO> mockReports = List.of(new MonthlyAmountReportDTO("March", 12000.0, 3));
        when(payrollService.getMonthlyAmountReport()).thenReturn(mockReports);

        mockMvc.perform(get("/api/payroll/monthly-amount")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].month").value("March"))
                .andExpect(jsonPath("$[0].totalAmount").value(12000.0));
    }
}

