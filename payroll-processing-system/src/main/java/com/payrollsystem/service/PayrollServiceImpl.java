package com.payrollsystem.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payrollsystem.dto.*;
import com.payrollsystem.exception.CustomExceptionEnum;
import com.payrollsystem.response.PayrollProcessingResponse;
import com.payrollsystem.util.ObjectMapperUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PayrollServiceImpl implements PayrollService {

    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MM");
    private static final Logger LOGGER = LogManager.getLogger(PayrollServiceImpl.class);
    private static final Path DATA_FILE_PATH = Paths.get("data", "EmployeeData.json");

    @Override
    public PayrollProcessingResponse processFile(final MultipartFile file) {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            final List<EventDTO> records = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                final EventDTO eventDTO = parseLineToEvent(line);
                if (eventDTO != null) {
                    records.add(eventDTO);
                    LOGGER.debug("Parsed EventDTO: {}", eventDTO);
                }
            }
            LOGGER.debug("Total records to write from this file: {}", records.size());
            writeRecordsToFile(records);
            return new PayrollProcessingResponse(true);
        } catch (final Exception e) {
            LOGGER.error("Error processing file", e);
            return new PayrollProcessingResponse(CustomExceptionEnum.INTERNAL_EXCEPTION.getResponse());
        }
    }

    @Override
    public List<MonthlyReportDTO> generateMonthlySalaryReport() throws IOException {
        final List<EventDTO> records = readRecordsFromFile();
        return records.stream()
                .filter(r -> "SALARY".equals(r.getEvent()))
                .collect(Collectors.groupingBy(
                        r -> (r.getEventDate()).getMonth().toString(),
                        Collectors.summarizingDouble(EventDTO::getValue)
                ))
                .entrySet().stream()
                .map(e -> new MonthlyReportDTO(e.getKey(), e.getValue().getSum(), (int) e.getValue().getCount()))
                .collect(Collectors.toList());
    }

    @Override
    public List<YearlyFinancialReportDTO> generateYearlyFinancialReport() throws IOException {
        final List<EventDTO> records = readRecordsFromFile();
        return records.stream()
                .map(r -> new YearlyFinancialReportDTO(r.getEvent(), r.getEmpId(),
                        Date.from(r.getEventDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        r.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public Integer getTotalEmployees() throws IOException {
        return new HashSet<>(readRecordsFromFile().stream().map(EventDTO::getEmpId).collect(Collectors.toList())).size();
    }

    @Override
    public Map<String, List<EventDTO>> getEmployeeEventsByMonth(final String event) throws IOException {
        return readRecordsFromFile().stream()
                .filter(r -> event.equals(r.getEvent()))
                .collect(Collectors.groupingBy(
                        r -> r.getEventDate().format(MONTH_FORMATTER)
                ));
    }

    @Override
    public List<EmployeeFinancialReportDTO> getEmployeeFinancialReport() throws IOException {
        return readRecordsFromFile().stream()
                .collect(Collectors.groupingBy(EventDTO::getEmpId))
                .entrySet().stream()
                .map(e -> {
                    final double totalAmount = e.getValue().stream()
                            .filter(v -> "SALARY".equals(v.getEvent()) || "BONUS".equals(v.getEvent())
                                    || "REIMBURSEMENT".equals(v.getEvent()))
                            .mapToDouble(EventDTO::getValue)
                            .sum();
                    final EventDTO first = e.getValue().get(0);
                    return new EmployeeFinancialReportDTO(first.getEmpId(), first.getFirstName(), first.getLastName(), totalAmount);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<MonthlyAmountReportDTO> getMonthlyAmountReport() throws IOException {
        return readRecordsFromFile().stream()
                .filter(r -> Arrays.asList("SALARY", "BONUS", "REIMBURSEMENT").contains(r.getEvent()))
                .collect(Collectors.groupingBy(
                        r -> r.getEventDate().format(MONTH_FORMATTER),
                        Collectors.summarizingDouble(EventDTO::getValue)
                ))
                .entrySet().stream()
                .map(e -> new MonthlyAmountReportDTO((String) e.getKey(), e.getValue().getSum(), (int) e.getValue().getCount()))
                .collect(Collectors.toList());
    }

    /**
     * Writes a list of event records to a JSON file.
     *
     * @param records the list of event data transfer objects to write
     * @throws IOException if there's an error writing to the file
     */
    private synchronized void writeRecordsToFile(final List<EventDTO> records) throws IOException {
        final File file = DATA_FILE_PATH.toFile();
        final ObjectMapper mapper = ObjectMapperUtil.getMapper();
        if (!file.exists()) {
            final boolean dirsMade = file.getParentFile().mkdirs();
            if (!dirsMade && !file.getParentFile().exists()) {
                throw new IOException("Failed to create directories for data file");
            }
            final boolean fileCreated = file.createNewFile();
            if (!fileCreated && !file.exists()) {
                throw new IOException("Failed to create data file");
            }
        }
        try {
            List<EventDTO> existingRecords = new ArrayList<>();
            if (file.length() > 0) {
                existingRecords = mapper.readValue(file, new TypeReference<>() {
                });
                LOGGER.info("Read {} existing records.", existingRecords.size());
            }
            existingRecords.addAll(records);
            mapper.writeValue(file, existingRecords);
            LOGGER.info("Successfully written {} total records to file.", existingRecords.size());
            final List<EventDTO> writtenRecords = mapper.readValue(file, new TypeReference<>() {
            });
            LOGGER.info("Read back {} records successfully.", writtenRecords.size());
        } catch (final Exception e) {
            LOGGER.error("Failed to write records to file", e);
        }
    }

    /**
     * Reads event records from a JSON file.
     *
     * @return a list of event data transfer objects
     * @throws IOException if there's an error reading from the file
     */
    private List<EventDTO> readRecordsFromFile() throws IOException {
        final File file = DATA_FILE_PATH.toFile();
        final ObjectMapper mapper = ObjectMapperUtil.getMapper();
        LOGGER.info(">>> File exists ? {}, File length: {}", file.exists(), file.length());
        if (file.exists() && file.length() > 0) {
            try {
                return mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, EventDTO.class));
            } catch (final IOException e) {
                LOGGER.error("Error reading from file, possibly due to formatting: {}", e.getMessage());
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Parses a single line from the file into an event data transfer object.
     *
     * @param line the line to parse
     * @return the event data transfer object, or null if the line is a header or invalid
     */
    private EventDTO parseLineToEvent(final String line) {
        if (line.startsWith("SequenceNo")) {
            return null;
        }
        final String[] data = line.split(",", -1);
        final EventDTO event = new EventDTO();
        if (data.length < 9) {
            LOGGER.error("Incomplete data line: {}, skipping it", line);
            return null;
        }
        try {
            return new EventDTO(
                    data[0].trim(), data[1].trim(), data[2].trim(),
                    data[3].trim(), data[4].trim(), data[5].trim(),
                    Double.parseDouble(data[6].trim()),
                    LocalDate.parse(data[7].trim(), DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                    cleanNotes(data[8].trim())
            );
        } catch (final NumberFormatException e) {
            LOGGER.error("Error parsing numeric fields", e);
            return null;
        }
    }

    /**
     * Cleans the notes field from the file, removing surrounding quotes.
     *
     * @param notes the notes string to clean
     * @return the cleaned notes string
     */
    private String cleanNotes(final String notes) {
        if (notes.startsWith("\"") && notes.endsWith("\"")) {
            return notes.substring(1, notes.length() - 1);
        }
        return notes;
    }
}
