package net.yorksolutions.feedbackapi.services;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import net.yorksolutions.feedbackapi.dtos.ErrorResponse;
import net.yorksolutions.feedbackapi.dtos.FeedbackRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;

/**
 * Handle all User Input Validation.
 * <p>
 * - `convertBindingResultToErrorResponse()` addresses issues thrown by
 * Spring Validator (basic input checking and regular expressions)
 * <p>
 * - `unreadableMessageHandler()` addresses issues thrown by JackSON in the
 * event that there are any deserialization problems
 * <p>
 * - `ratingOmittedHandler()` addresses a scenario where the User does not
 * provide the `rating` whatsoever
 * <p>
 * - `dataFieldsOmittedHandler()` addresses a scenario where the User does
 * not provide all necessary fields.
 */
@Service
public class ValidationException {

    public ErrorResponse convertBindingResultToErrorResponse(
            BindingResult invalidUserInputError
    ) {
        // DESC: Map for 'field' - 'message' (from BindingResult)
        HashMap<String, String> errorFieldsAndMessages = new HashMap<>();

        // DESC: Loop through FieldErrors and update Map
        for (FieldError fieldError : invalidUserInputError.getFieldErrors()) {
            // DESC: Add error(s) (field & message) to the Client-friendly DTO
            errorFieldsAndMessages.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        // DESC: Create ErrorResponse (with HashMap data)
        ErrorResponse userValidErrResponse = new ErrorResponse(
                "Errors", "Validation Failed",  errorFieldsAndMessages
        );

        return userValidErrResponse;
    }


    public ResponseEntity<String> unreadableMessageHandler(
            Throwable causeOfException
    ) {
        // DESC: Example `FeedbackRequest` for toString repr.
        FeedbackRequest exampleRequest = new FeedbackRequest(
                "123abc", "Dr. John Doe",
                "5", "A great doctor!"
        );

        // DESC: Address variety of HttpMessageNotReadableException errors
        if (causeOfException instanceof JsonParseException) {
            return ResponseEntity
                    .status(400)
                    .body(
                            "JSON is syntactically invalid. Please provide JSON like the following:\n" + exampleRequest.toString()
                    );
        } else if (causeOfException instanceof MismatchedInputException) {
            return ResponseEntity
                    .status(400)
                    .body(
                            "A data mismatched has been encountered on the provided input. Please provide JSON like the following:\n"
                                    + exampleRequest.toString()
                    );
        } else if (causeOfException instanceof UnrecognizedPropertyException) {
            return ResponseEntity
                    .status(400)
                    .body(
                            "An additional property was encountered in the provided JSON. Please provide JSON like the following:\n" + exampleRequest.toString()
                    );
        } else if (causeOfException instanceof JsonMappingException) {
            return ResponseEntity
                    .status(400)
                    .body(
                            "Multiple issues encountered with provided JSON. Possibly due to issues mapping the fields provided and/or incorrect data-types. Please provide JSON like the following:\n" + exampleRequest.toString()
                    );
        } else {
            return ResponseEntity
                    .status(400)
                    .body(
                            "An unknown exception, around user input, has been encountered. Please provide JSON like the following:\n" + exampleRequest.toString()
                    );
        }
    }

    public ResponseEntity<String> ratingOmittedHandler() {
        // DESC: Example `FeedbackRequest` for toString repr.
        FeedbackRequest exampleRequest = new FeedbackRequest(
                "123abc", "Dr. John Doe",
                "5", "A great doctor!"
        );

        return ResponseEntity
                .status(400)
                .body(
                        "The `rating` value appears to have been omitted. Please provide JSON like the following:\n" + exampleRequest.toString()
                );
    }

    public ResponseEntity<String> dataFieldsOmittedHandler() {
        // DESC: Example `FeedbackRequest` for toString repr.
        FeedbackRequest exampleRequest = new FeedbackRequest(
                "123abc", "Dr. John Doe",
                "5", "A great doctor!"
        );

        return ResponseEntity
                .status(400)
                .body(
                        "One or more fields appear to be missing from the JSON payload. Please provide JSON like the following:\n" + exampleRequest.toString()
                );
    }
}