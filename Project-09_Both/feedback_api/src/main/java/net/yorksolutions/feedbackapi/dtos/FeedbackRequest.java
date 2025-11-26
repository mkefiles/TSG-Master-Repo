package net.yorksolutions.feedbackapi.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

/**
 * A DTO class that handles/validates the received JSON
 * payload from the Client. Note that this will reject any
 * unknown properties that are encountered, due to faulty
 * Client-input (i.e., too many values in the JSON)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeedbackRequest {

    /*
     * SECTION: DTO / POJO Variables
     * - General Note: This is using RegEx to better-validate the input
     * ... because JackSON appears to type-coerce an Integer into a String
     * ... without throwing errors, yet it throws an HttpMessageNotReadable
     * ... Exception when a String is provided where an Integer is expected.
     * - General Note: Per spec, there is NO enforcement on the alphanumerical
     * ... requirement, alphabetical requirement, or a minimum of three. That
     * ... was simply added to better mirror the example JSON payload AND to
     * ... have more of a universal catch-all.
     * - General Note: The `rating` field, in so far as this DTO, has been
     * ... changed from an Integer to a String as it is not possible to run
     * ... RegEx on an Integer. Making this change creates better validation
     * ... enforcement and, with RegEx, ensures that the proper error message
     * ... is thrown regardless of the provided input.
     */

    @Pattern(
            regexp = "(?=.*[a-zA-Z])^[a-zA-Z0-9-]{3,36}$",
            message = "memberId MUST be between 3 and 36 characters AND use alphanumeric characters (e.g., m-123)"
    )
    @Schema(example = "m-123", description = "Alphanumerical Member ID")
    private String memberId;

    @Pattern(
            regexp = "^[a-zA-Z][a-zA-Z\s.-]{1,78}[a-zA-Z]$",
            message = "providerName MUST be between 3 and 80 characters. It MAY contain periods '.' or dashes '-', however they CANNOT be at the start OR the end."
    )
    @Schema(example = "Dr. Gregg Trunnell", description = "Doctor / Provider / Physician Name")
    private String providerName;

    @Pattern(
            regexp = "^[12345]$",
            message = "rating MUST be a value between 1 and 5 (inclusive)."
    )
    @Schema(example = "5", description = "Rating from 1 - 5 (Passed as String or Integer)")
    private String rating;

    @Size(
            max = 200, message = "comment, IF provided, MUST be <= 200 characters"
    )
    @NotNull(
            message = "comment can be empty, however it CANNOT be null."
    )
    @Schema(example = "A great doc. He heard all my health-concerns and was very helpful!", description = "An Brief Comment (Optional)")
    private String comment;

    /*
     * SECTION: No-Args Constructor
     */

    public FeedbackRequest() {}

    /*
     * SECTION: All-Args Constructor
     */

    public FeedbackRequest(
            String memberId, String providerName,
            String rating, String comment
    ) {
        this.memberId = memberId;
        this.providerName = providerName;
        this.rating = rating;
        this.comment = comment;
    }

    /*
     * SECTION: Getter / Setter Methods
     */

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /*
     * SECTION: toString Override
     */

    @Override
    public String toString() {
        return "{" +
                " \"memberId\": " + "\"" + memberId + "\"," +
                " \"providerName\": " + "\""  + providerName + "\"," +
                " \"rating\": " + "\""  + rating + "\"," +
                " \"comment\": " + "\""  + comment + "\"" +
                " }";
    }
}

