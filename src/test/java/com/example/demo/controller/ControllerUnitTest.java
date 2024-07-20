package com.example.demo.controller;

import com.example.demo.dto.ExerciseDTO;
import com.example.demo.entity.Exercise;
import com.example.demo.exceptions.ResponseMessage;
import com.example.demo.service.ServiceCSV;
import com.example.demo.util.ExerciseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ControllerUnitTest {

    @Mock
    private ServiceCSV service;

    @InjectMocks
    private Controller controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUploadData_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "some,csv,content".getBytes());
        when(service.uploadData(any(MultipartFile.class))).thenReturn("Successfully saved the data");

        ResponseEntity<ResponseMessage> response = controller.uploadData(file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("File uploaded successfully", response.getBody().getMessage());
    }

    @Test
    public void testUploadData_EmptyFile() {
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "".getBytes());

        ResponseEntity<ResponseMessage> response = controller.uploadData(file);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("CSV file is empty", response.getBody().getMessage());
    }


    @Test
    public void testGetData_Success() {
        Exercise exercise = new Exercise();
        exercise.setCode("123");
        when(service.getByCode("123")).thenReturn(exercise);
        ExerciseDTO exerciseDTO = ExerciseMapper.INSTANCE.toDTO(exercise);

        ResponseEntity<?> response = controller.getData("123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(exerciseDTO, response.getBody());
    }

    @Test
    public void testGetData_NotFound() {
        when(service.getByCode("123")).thenReturn(null);

        ResponseEntity<?> response = controller.getData("123");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No data found for the requested code", ((ResponseMessage) response.getBody()).getMessage());
    }

    @Test
    public void testDeleteAll_Success() {
        when(service.isDataPresent()).thenReturn(true);
        when(service.deleteAll()).thenReturn("All records deleted");

        ResponseEntity<ResponseMessage> response = controller.deleteAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("All records deleted", response.getBody().getMessage());
    }

    @Test
    public void testDeleteAll_NotFound() {
        when(service.isDataPresent()).thenReturn(false);

        ResponseEntity<ResponseMessage> response = controller.deleteAll();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No data found to delete", response.getBody().getMessage());
    }
}
