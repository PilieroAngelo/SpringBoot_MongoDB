package com.azienda.Exceptions;

import io.swagger.v3.oas.annotations.Hidden;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Hidden
@ControllerAdvice
public class GlobalExceptionHandler extends Throwable {
    private static final Logger log = LogManager.getLogger(GlobalExceptionHandler.class);

    private String getCurrentFormattedDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private ErrorDetails buildErrorDetails(String message, String description, String errorCode) {
        return new ErrorDetails(getCurrentFormattedDate(), message, description, errorCode);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception,WebRequest webRequest) {

        log.error("Handled Exception: MethodArgumentNotValidException - {}", exception.getMessage());

        String fieldErrors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ErrorDetails errorDetails = buildErrorDetails("Uno o più campi non sono validati",
                "Campi con validazione errata: " + fieldErrors, "BAD_REQUEST_ARGUMENT_NOT_VALID");

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    //errore generico 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handlerGenericException(Exception exception, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(getCurrentFormattedDate(), exception.getMessage(), webRequest.getDescription(false),"INTERNAL SERVER ERROR");
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleIdNotFoundException(IdNotFoundException e, WebRequest request) {
        log.error("Handled Exception: IdNotFoundException - {}", e.getMessage());
        ErrorDetails errorDetails = buildErrorDetails(e.getMessage(),
                request.getDescription(false), "ID NON TROVATO");
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}
