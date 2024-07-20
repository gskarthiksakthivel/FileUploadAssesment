package com.example.demo.service;

import com.example.demo.dto.ExerciseDTO;
import com.example.demo.entity.Exercise;
import com.example.demo.exceptions.FileStoreException;
import com.example.demo.repo.ExerciseRepo;
import com.example.demo.util.ExerciseMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceCSV {

    private static final Logger log = LoggerFactory.getLogger(ServiceCSV.class);

    private final ExerciseRepo repo;
    private static final String ALL_RECORDS_DELETED = "All records deleted";
    private static final String FAILED_TO_SAVE_DATA = "Failed to save data";
    private static final String SUCCESSFULLY_SAVED_THE_DATA = "Successfully saved the data";

    public ServiceCSV(ExerciseRepo repo) {
        this.repo = repo;
    }

    /**
     * Method to upload data
     * @param file
     * @return
     */
    public String uploadData(MultipartFile file) {
        List<Exercise> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim());
            csvParser.getRecords().forEach(record -> {
                Exercise exercise = new Exercise();
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                try {
                    exercise.setCode(record.get("code"));
                    exercise.setSource(record.get("source"));
                    exercise.setCodeListCode(record.get("codeListCode"));
                    exercise.setLongDescription(record.get("longDescription"));
                    exercise.setDisplayValue(record.get("displayValue"));
                    exercise.setFromDate(
                            record.get("fromDate") == null || record.get("fromDate").trim().isEmpty()
                                    ? null
                                    : LocalDate.parse(record.get("fromDate"), dateTimeFormatter)
                    );
                    exercise.setToDate(
                            record.get("toDate") == null || record.get("toDate").trim().isEmpty()
                                    ? null
                                    : LocalDate.parse(record.get("toDate"), dateTimeFormatter)
                    );
                    exercise.setSortingPriority(
                            Integer.parseInt(record.get("sortingPriority") == null || record.get("sortingPriority").trim().isEmpty()
                                    ? "0"
                                    : record.get("sortingPriority"))
                    );
                    data.add(exercise);
                } catch (Exception e) {
                    log.error("Error processing record: {}", record, e);
                }
            });
        } catch (IOException e) {
            log.error("Failed to read CSV file", e);
            throw new FileStoreException(FAILED_TO_SAVE_DATA);
        }
        try {
            repo.saveAll(data);
            log.info("Data saved successfully");
        } catch (Exception e) {
            log.error("Failed to save data to the repository", e);
            throw new FileStoreException(FAILED_TO_SAVE_DATA);
        }
        return SUCCESSFULLY_SAVED_THE_DATA;
    }

    /**
     * Method to get all data
     * @return
     */
    public List<ExerciseDTO> getAllData() {
        log.debug("Fetching all data");
        return repo.findAll().stream()
                .map(ExerciseMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Method to get by code
     * @param code
     * @return
     */
    public Exercise getByCode(String code) {
        log.debug("Fetching data for code: {}", code);
        Exercise exercise = repo.findBycode(code);
        if (exercise == null) {
            log.warn("No data found for code: {}", code);
        } else {
            log.debug("Data found for code: {}", code);
        }
        return exercise;
    }

    /**
     * Checks if there is any data present in the repository.
     *
     * This method counts the number of records in the repository and returns true if
     * there is at least one record present, otherwise, it returns false.
     *
     * @return true if there are records present in the repository, false otherwise
     */
    public boolean isDataPresent() {
        long count = repo.count();
        log.debug("Data present count: {}", count);
        return count > 0;
    }


    /**
     * Method to delete All
     * @return
     */
    public String deleteAll() {
        log.info("Deleting all records");
        try {
            repo.deleteAll();
            log.info("All records deleted successfully");
        } catch (Exception e) {
            log.error("Failed to delete all records", e);
            throw new FileStoreException("Failed to delete all records");
        }
        return ALL_RECORDS_DELETED;
    }
}
