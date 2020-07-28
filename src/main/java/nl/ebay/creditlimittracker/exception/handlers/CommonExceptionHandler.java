package nl.ebay.creditlimittracker.exception.handlers;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Exception Handler helps to customize error code and the body for the response
 * based on different exception for common nl.jumbo.storemanager.service runtime exceptions
 */

@ControllerAdvice
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Customize the response for JsonConversionException.
     * <p>
     * This method logs a warning, sets the "Allow" header, and delegates to *
     * {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */

    @ExceptionHandler(JsonConversionException.class)
    private ResponseEntity<Object> handleJSONExceptionInternal(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, "The execution of the service failed while JSON conversion.Please try again",
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Customize the response for {@link FileParsingException}
     *
     * @param ex      the exception
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @ExceptionHandler({FileParsingException.class})
    private ResponseEntity<Object> handleFileParsingOrReadingExc(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, "The execution of the service failed while reading the file.Please try again",
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Customize the response for {@link ConstraintViolationException}
     *
     * @param ex      the exception
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @ExceptionHandler(ConstraintViolationException.class)
    private ResponseEntity<Object> handleValidations(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, "The Request is missing required fields",
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<Violation> errors = new ArrayList<>();

        errors.addAll(ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new Violation(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList()));

        errors.addAll(ex.getBindingResult().getGlobalErrors().stream()
                .map(fieldError -> new Violation(fieldError.getCode(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList()));


        return new ResponseEntity<>(
                ErrorDetails.builder().timestamp(new Date()).message("Validation failed").violations(errors).build(),
                HttpStatus.BAD_REQUEST
        );
    }


}
