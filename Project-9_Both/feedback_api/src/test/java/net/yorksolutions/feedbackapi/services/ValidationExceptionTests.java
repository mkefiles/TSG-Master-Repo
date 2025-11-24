package net.yorksolutions.feedbackapi.services;

import com.fasterxml.jackson.core.JsonParseException;
import net.yorksolutions.feedbackapi.dtos.ErrorResponse;
import net.yorksolutions.feedbackapi.dtos.FeedbackRequest;
import net.yorksolutions.feedbackapi.services.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for Validation Exception (Service Layer)")
public class ValidationExceptionTests {

    /*
     * SECTION: Service / Repository Mocking
     */

    // DESC: Initialize subject-file 'mock'
    @InjectMocks
    private ValidationException mockedValidationException;

    /**
     * The following test basically ensures that, when a BindingResult
     * is given a FieldError, it will properly map to the ErrorResponse
     * DTO that was created for the Client to receive.
     */
    @Test
    @DisplayName("Ensure that BindingResult returns no errors")
    public void convertBindingResultToErrorResponse_returnErrorResponse_givenFieldError() {
        /* SECTION: Given Input Arrange[ment]... */

        HashMap<String, String> dummyErrorFieldsAndMessages = new HashMap<>();
        dummyErrorFieldsAndMessages.put(
                "memberId",
                "memberId MUST be between 3 and 36 characters AND use alphanumeric characters (e.g., m-123)"
        );

        // NOTE: The `objectName` is the name of the target-object, which
        // ... is a `FeedbackRequest` in this example -- for testing purposes
        // ... the name itself is NOT relevant... just aim to be consistent
        FieldError dummyFieldError = new FieldError(
                "feedbackRequest", "memberId",
                "memberId MUST be between 3 and 36 characters AND use alphanumeric characters (e.g., m-123)"
        );

        // NOTE: The `objectName` is the name of the target-object, which
        // ... is a `FeedbackRequest` in this example -- for testing purposes
        // ... the name itself is NOT relevant... just aim to be consistent
        BindingResult dummyBindingResult = new BeanPropertyBindingResult(
                new FeedbackRequest(), "feedbackRequest"
        );

        dummyBindingResult.addError(dummyFieldError);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                "Errors", "Validation Failed", dummyErrorFieldsAndMessages
        );

        /* SECTION: When Act[ed] Upon... */

        ErrorResponse actualErrorResponse = mockedValidationException
                .convertBindingResultToErrorResponse(dummyBindingResult);

        /* SECTION: Then Assert Output Is... */

        // NOTE: This is testing against all properties within the subject
        // ... Object because there is no unique ID-field to compare against
        Assertions.assertAll("ErrorResponse Properties",
                () -> Assertions.assertEquals(expectedErrorResponse.getStatus(), actualErrorResponse.getStatus()),
                () -> Assertions.assertEquals(expectedErrorResponse.getMessage(), actualErrorResponse.getMessage()),
                () -> Assertions.assertEquals(expectedErrorResponse.getErrors(), actualErrorResponse.getErrors())
        );
    }

    @Test
    @DisplayName("Ensure error-handling works (JsonParseException)")
    public void unreadableMessageHandler_returnError_givenBadJson() {
        /* SECTION: Given Input Arrange[ment]... */

        FeedbackRequest dummyExampleRequest = new FeedbackRequest(
                "123abc", "Dr. John Doe",
                "5", "A great doctor!"
        );

        JsonParseException dummyExceptionCause =
                new JsonParseException("msg. value is not applicable to test");

        ResponseEntity<String> expectedResponse =
                new ResponseEntity(
                        "JSON is syntactically invalid. Please provide JSON like the following:\n"
                        + dummyExampleRequest.toString(), HttpStatusCode.valueOf(400)
                );

        /* SECTION: When Act[ed] Upon... */

        ResponseEntity<String> actualResponse = mockedValidationException.unreadableMessageHandler(dummyExceptionCause);

        /* SECTION: Then Assert Output Is... */

        Assertions.assertAll(
                () -> Assertions.assertEquals(
                        expectedResponse.getBody(), actualResponse.getBody()),
                () -> Assertions.assertEquals(
                        expectedResponse.getStatusCode(), actualResponse.getStatusCode())
        );
    }

    @Test
    @DisplayName(
            "Ensure error-handling works (Unhandled HttpMessageNotReadableException Error)"
    )
    public void unreadableMessageHandler_returnError_givenUnknownError() {
        /* SECTION: Given Input Arrange[ment]... */

        FeedbackRequest dummyExampleRequest = new FeedbackRequest(
                "123abc", "Dr. John Doe",
                "5", "A great doctor!"
        );

        Throwable dummyExceptionCause =
                new Throwable("msg. value is not applicable to test");

        ResponseEntity<String> expectedResponse =
                new ResponseEntity(
                        "An unknown exception, around user input, has been encountered. Please provide JSON like the following:\n"
                        + dummyExampleRequest.toString(), HttpStatusCode.valueOf(400)
                );

        /* SECTION: When Act[ed] Upon... */

        ResponseEntity<String> actualResponse = mockedValidationException.unreadableMessageHandler(dummyExceptionCause);

        /* SECTION: Then Assert Output Is... */

        Assertions.assertAll(
                () -> Assertions.assertEquals(
                        expectedResponse.getBody(), actualResponse.getBody()),
                () -> Assertions.assertEquals(
                        expectedResponse.getStatusCode(), actualResponse.getStatusCode())
        );
    }

    @Test
    @DisplayName("Ensure proper response for `ratingOmittedHandler()`")
    public void ratingOmittedHandler_returnDeterministicErr_whenCalled() {
        /* SECTION: Given Input Arrange[ment]... */

        FeedbackRequest dummyExampleRequest = new FeedbackRequest(
                "123abc", "Dr. John Doe",
                "5", "A great doctor!"
        );

        ResponseEntity<String> expectedResponse =
                new ResponseEntity(
                        "The `rating` value appears to have been omitted. Please provide JSON like the following:\n"
                        + dummyExampleRequest.toString(), HttpStatusCode.valueOf(400)
                );

        /* SECTION: When Act[ed] Upon... */

        ResponseEntity<String> actualResponse = mockedValidationException
                .ratingOmittedHandler();

        /* SECTION: Then Assert Output Is... */

        Assertions.assertAll(
                () -> Assertions.assertEquals(
                        expectedResponse.getBody(), actualResponse.getBody()),
                () -> Assertions.assertEquals(
                        expectedResponse.getStatusCode(), actualResponse.getStatusCode())
        );
    }

    @Test
    @DisplayName("Ensure proper response for `dataFieldsOmittedHandler()`")
    public void dataFieldsOmittedHandler_returnDeterministicErr_whenCalled() {
        /* SECTION: Given Input Arrange[ment]... */

        FeedbackRequest dummyExampleRequest = new FeedbackRequest(
                "123abc", "Dr. John Doe",
                "5", "A great doctor!"
        );

        ResponseEntity<String> expectedResponse =
                new ResponseEntity(
                        "One or more fields appear to be missing from the JSON payload. Please provide JSON like the following:\n"
                        + dummyExampleRequest.toString(), HttpStatusCode.valueOf(400)
                );

        /* SECTION: When Act[ed] Upon... */

        ResponseEntity<String> actualResponse = mockedValidationException
                .dataFieldsOmittedHandler();

        /* SECTION: Then Assert Output Is... */

        Assertions.assertAll(
                () -> Assertions.assertEquals(
                        expectedResponse.getBody(), actualResponse.getBody()),
                () -> Assertions.assertEquals(
                        expectedResponse.getStatusCode(), actualResponse.getStatusCode())
        );
    }
}


//    @Test
//    @DisplayName("...")
//    public void funcName_returnExpectation_expectedOutput() {
//        /* SECTION: Given Input Arrange[ment]... */
//
//        /* SECTION: When Act[ed] Upon... */
//
//        /* SECTION: Then Assert Output Is... */
//    }