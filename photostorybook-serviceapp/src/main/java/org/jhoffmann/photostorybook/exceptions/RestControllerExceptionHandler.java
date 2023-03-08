package org.jhoffmann.photostorybook.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.jhoffmann.photostorybook.api.v1.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class RestControllerExceptionHandler {
    @ExceptionHandler( value = {ApiRequestException.class})
    public ResponseEntity<ApiError> handleApiException(ApiRequestException e) {
        //ApiException apiException = new ApiException(e.getMessage(), e, HttpStatus.BAD_REQUEST, LocalDateTime.now());
        log.error("ApiRequestException Message: " + e.getMessage());
        log.error("ApiRequestException Stacktrace: " + e.toString());
        ApiError apiError = new ApiError();
        apiError.setCode(HttpStatus.BAD_REQUEST.value());
        apiError.setMessage(e.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
