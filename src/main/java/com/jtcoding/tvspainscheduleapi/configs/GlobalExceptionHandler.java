package com.jtcoding.tvspainscheduleapi.configs;

import com.jtcoding.tvspainscheduleapi.exceptions.EventException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(EventException.class)
    ProblemDetail eventNotFound(EventException e) {
        ProblemDetail problemDetail =
                ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "Event not found");
        problemDetail.setTitle("Event Not Found");
        log.error(e.toString());
        return problemDetail;
    }

}
