package com.markets.demo.shared.exception;

import com.markets.demo.business.dto.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Arrays;

@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        ResponseError responseError =
                ResponseError.builder().message(ex.getMessage()).timeStamp(LocalDateTime.now()).title("Resource is not found")
                        .build();

        return new ResponseEntity<>(responseError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        ResponseError responseError =
                ResponseError.builder().message(constraintViolationMessageBuilder(ex)).timeStamp(LocalDateTime.now()).title("Constraint Violation Error")
                        .build();

        return new ResponseEntity<>(responseError, HttpStatus.BAD_REQUEST);
    }

    private String constraintViolationMessageBuilder(ConstraintViolationException ex) {
        String[] messages;

        if (ex.getMessage().contains("Query")) {
            String[] parts = ex.getMessage().split(":");
            return parts[1];
        }

        if (ex.getConstraintViolations().size() > 0) {
            messages = new String[ex.getConstraintViolations().size()];
            int i = 0;

            for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
                messages[i++] = violation.getPropertyPath() + " " + violation.getMessage();
            }

            return Arrays.toString(messages);
        }

        return "Invalid input";
    }

    @ExceptionHandler({org.hibernate.exception.ConstraintViolationException.class})
    public ResponseEntity<?> handleDatabaseViolationException(org.hibernate.exception.ConstraintViolationException ex, HttpServletRequest request) {
        ResponseError responseError =
                ResponseError.builder().message("There is an already saved entity with the same attributes").timeStamp(LocalDateTime.now()).title("Constraint Violation Error")
                        .build();

        return new ResponseEntity<>(responseError, HttpStatus.BAD_REQUEST);
    }
}
