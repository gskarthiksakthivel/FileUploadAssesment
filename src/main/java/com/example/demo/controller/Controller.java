package com.example.demo.controller;

import com.example.demo.dto.ExerciseDTO;
import com.example.demo.entity.Exercise;
import com.example.demo.exceptions.FileStoreException;
import com.example.demo.exceptions.ResponseMessage;
import com.example.demo.service.ServiceCSV;
import com.example.demo.util.ExerciseMapper;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api")
public class Controller {

    private final ServiceCSV service;
    private static final String CSV_FILE_EMPTY = "CSV file is empty";
    private static final String FILE_UPLOADED_SUCCESSFULLY = "File uploaded successfully";
    private static final String NO_DATA_FOUND_FOR_REQUESTED_CODE = "No data found for the requested code";
    private static final String FAILED_TO_UPLOAD_FILE = "Failed to upload file";
    private static final String NO_DATA_FOUND_TO_DELETE = "No data found to delete";

    public Controller(ServiceCSV service) {
        this.service = service;
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadData(@RequestParam("file") @Valid MultipartFile file) {
        log.info("Received a file upload request");
        if (file.isEmpty()) {
            log.warn("Received an empty file");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage(CSV_FILE_EMPTY));
        }
        try {
            service.uploadData(file);
            log.info("File uploaded successfully");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage(FILE_UPLOADED_SUCCESSFULLY));
        } catch (FileStoreException e) {
            log.error(FAILED_TO_UPLOAD_FILE, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage(FAILED_TO_UPLOAD_FILE));
        }
    }


    @GetMapping("/fetchAll")
    public ResponseEntity<List<ExerciseDTO>> getAllData() {
        log.info("Received a request to fetch all data");
        List<ExerciseDTO> data = service.getAllData();
        log.debug("Fetched data: {}", data);
        return ResponseEntity.ok(data);
    }


    @GetMapping("/fetchByCode/{code}")
    public ResponseEntity<?> getData(@PathVariable @Valid @NotEmpty String code) {
        log.info("Received a request to fetch data for code: {}", code);
        Optional<Exercise> exercise = Optional.ofNullable(service.getByCode(code));
        if (exercise.isPresent()) {
            ExerciseDTO exerciseDTO = ExerciseMapper.INSTANCE.toDTO(exercise.get());
            log.info("Data found for code: {}", code);
            log.debug("Fetched data: {}", exerciseDTO);
            return ResponseEntity.status(HttpStatus.OK).body(exerciseDTO);
        } else {
            log.warn("No data found for code: {}", code);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(NO_DATA_FOUND_FOR_REQUESTED_CODE));
        }
    }


    @DeleteMapping("/deleteAll")
    public ResponseEntity<ResponseMessage> deleteAll() {
        log.info("Received a request to delete all data");
        boolean isDataPresent = service.isDataPresent();
        if (!isDataPresent) {
            log.warn("No data found to delete");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage(NO_DATA_FOUND_TO_DELETE));
        }
        String message = service.deleteAll();
        log.info("All data deleted successfully");
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    }
}
