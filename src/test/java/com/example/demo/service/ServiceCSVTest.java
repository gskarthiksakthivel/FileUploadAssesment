package com.example.demo.service;

import com.example.demo.dto.ExerciseDTO;
import com.example.demo.entity.Exercise;
import com.example.demo.exceptions.FileStoreException;
import com.example.demo.repo.ExerciseRepo;
import com.example.demo.util.ExerciseMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ServiceCSVTest {

    @Mock
    private ExerciseRepo repo;

    @InjectMocks
    private ServiceCSV serviceCSV;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUploadData_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "code,source,codeListCode,longDescription,displayValue,fromDate,toDate,sortingPriority\n123,source,codeListCode,description,displayValue,01-01-2021,01-01-2022,1".getBytes());

        String result = serviceCSV.uploadData(file);

        assertEquals("Successfully saved the data", result);
        verify(repo, times(1)).saveAll(any());
    }

    @Test
    public void testUploadData_FileStoreException() {
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "code,source,codeListCode,longDescription,displayValue,fromDate,toDate,sortingPriority\n".getBytes());

        doThrow(new RuntimeException("Failed to save")).when(repo).saveAll(any());

        FileStoreException exception = assertThrows(FileStoreException.class, () -> {
            serviceCSV.uploadData(file);
        });

        assertEquals("Failed to save data", exception.getMessage());
        verify(repo, times(1)).saveAll(any());
    }

    @Test
    public void testGetAllData() {
        List<Exercise> exercises = Arrays.asList(new Exercise(), new Exercise());
        when(repo.findAll()).thenReturn(exercises);

        List<ExerciseDTO> result = serviceCSV.getAllData();

        assertEquals(2, result.size());
        verify(repo, times(1)).findAll();
    }

    @Test
    public void testGetByCode_Success() {
        Exercise exercise = new Exercise();
        exercise.setCode("123");
        when(repo.findBycode("123")).thenReturn(exercise);

        Exercise result = serviceCSV.getByCode("123");

        assertNotNull(result);
        assertEquals("123", result.getCode());
        verify(repo, times(1)).findBycode("123");
    }

    @Test
    public void testGetByCode_NotFound() {
        when(repo.findBycode("123")).thenReturn(null);

        Exercise result = serviceCSV.getByCode("123");

        assertNull(result);
        verify(repo, times(1)).findBycode("123");
    }

    @Test
    public void testIsDataPresent() {
        when(repo.count()).thenReturn(5L);

        boolean result = serviceCSV.isDataPresent();

        assertTrue(result);
        verify(repo, times(1)).count();
    }

    @Test
    public void testIsDataPresent_NoData() {
        when(repo.count()).thenReturn(0L);

        boolean result = serviceCSV.isDataPresent();

        assertFalse(result);
        verify(repo, times(1)).count();
    }

    @Test
    public void testDeleteAll_Success() {
        String result = serviceCSV.deleteAll();

        assertEquals("All records deleted", result);
        verify(repo, times(1)).deleteAll();
    }

    @Test
    public void testDeleteAll_FileStoreException() {
        doThrow(new RuntimeException("Failed to delete")).when(repo).deleteAll();

        FileStoreException exception = assertThrows(FileStoreException.class, () -> {
            serviceCSV.deleteAll();
        });

        assertEquals("Failed to delete all records", exception.getMessage());
        verify(repo, times(1)).deleteAll();
    }
}
