package com.example.demo.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice(basePackages = "com.example.demo")
public class ExceptionHandlers {

    /**
     * Handles FileStoreException and returns a detailed API error response.
     *
     * @param ex      the thrown FileStoreException
     * @param request the web request during which the exception was thrown
     * @return a ResponseEntity containing the ApiError details
     */
    @ExceptionHandler(FileStoreException.class)
    public ResponseEntity<ApiError> handleCustomException(FileStoreException ex, WebRequest request) {
        log.error("FileStoreException occurred: {}", ex.getLocalizedMessage(), ex);

        List<String> errorDesc = new ArrayList<>();
        errorDesc.add(ex.getLocalizedMessage());

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.EXPECTATION_FAILED.value(),
                HttpStatus.EXPECTATION_FAILED.name(),
                errorDesc
        );

        return new ResponseEntity<>(apiError, HttpStatus.EXPECTATION_FAILED);
    }

    /**
     * Handles MultipartException and returns a detailed API error response.
     *
     * @param ex      the thrown MultipartException
     * @param request the web request during which the exception was thrown
     * @return a ResponseEntity containing the ApiError details
     */
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ApiError> handleMultipartException(MultipartException ex, WebRequest request) {
        log.error("MultipartException occurred: {}", ex.getLocalizedMessage(), ex);

        List<String> errorDesc = new ArrayList<>();
        errorDesc.add("Invalid file upload");

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                errorDesc
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles generic exceptions and returns a detailed API error response.
     *
     * @param ex      the thrown Exception
     * @param request the web request during which the exception was thrown
     * @return a ResponseEntity containing the ApiError details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex, WebRequest request) {
        log.error("Exception occurred: {}", ex.getLocalizedMessage(), ex);

        List<String> errorDesc = new ArrayList<>();
        errorDesc.add(ex.getLocalizedMessage() != null ? ex.getLocalizedMessage() : "An unexpected error occurred");

        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                errorDesc
        );

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
