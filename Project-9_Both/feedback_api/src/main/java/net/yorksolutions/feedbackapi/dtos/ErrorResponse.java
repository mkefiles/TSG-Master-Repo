package net.yorksolutions.feedbackapi.dtos;

import java.util.Map;

/**
 * A DTO class that bridges the BindingResult to a
 * Client-friendly error format in the event of an
 * input error. The format of a BindingResult error
 * will be the problematic field followed by the
 * applicable validation message assembled on the
 * `FeedbackRequest` DTO.
 */
public class ErrorResponse {
    /*
     * SECTION: DTO / POJO Variables
     */
    private String status;
    private String message;
    private Map<String, String> errors;

    /*
     * SECTION: No-Args Constructor
     */
    public ErrorResponse() {}

    /*
     * SECTION: All-Args Constructor
     */
    public ErrorResponse(String status, String message, Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    /*
     * SECTION: Getter / Setter Methods
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
