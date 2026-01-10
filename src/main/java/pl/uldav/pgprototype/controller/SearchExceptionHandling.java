package pl.uldav.pgprototype.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.uldav.pgprototype.controller.dto.ErrorResponseDTO;
import pl.uldav.pgprototype.exception.QueryValidationException;

@Slf4j
@ControllerAdvice
public class SearchExceptionHandling {
    @ExceptionHandler(QueryValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleException(QueryValidationException e) {
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(e.getMessage()));
    }
}
