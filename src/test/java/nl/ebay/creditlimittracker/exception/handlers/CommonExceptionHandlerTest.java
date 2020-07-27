package nl.ebay.creditlimittracker.exception.handlers;

import info.solidsoft.mockito.java8.api.WithBDDMockito;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CommonExceptionHandlerTest implements WithBDDMockito {

    private CommonExceptionHandler underTest;

    public CommonExceptionHandlerTest() {
        underTest = new CommonExceptionHandler();
    }

    @Mock
    private MethodArgumentNotValidException exception;
    @Mock
    private HttpHeaders headers;
    @Mock
    private WebRequest request;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private ObjectError objectError;

    @Test
    @DisplayName("Should return a bad request response entity with validation errors in body when MethodArgumentNotValidException is thrown by the validation framework")
    void happy() {
        // given
        List<ObjectError> errors = new ArrayList<>();
        errors.add(objectError);

        given(exception.getBindingResult()).willReturn(bindingResult);
        given(bindingResult.getGlobalErrors()).willReturn(errors);
        given(objectError.getCode()).willReturn("Code");
        given(objectError.getDefaultMessage()).willReturn("Message");

        // when
        ResponseEntity entity = underTest.handleMethodArgumentNotValid(exception, headers, HttpStatus.BAD_REQUEST, request);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());

        verify(exception, times(2)).getBindingResult();
        verify(bindingResult).getGlobalErrors();
        verify(bindingResult).getFieldErrors();
        verify(objectError).getCode();
        verify(objectError).getDefaultMessage();
        verifyNoMoreInteractions(exception, bindingResult, objectError);

    }
}