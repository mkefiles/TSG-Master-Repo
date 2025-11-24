package net.yorksolutions.feedbackapi.dtos;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * A DTO class that is used in the response to the Client.
 * This has a heavy one-to-one mapping with the Entity, however
 * it is *best-practice* to return a DTO not an Entity.
 */
public class FeedbackResponse {

    /*
     * SECTION: DTO / POJO Variables
     */
    private UUID id;
    private String memberId;
    private String providerName;
    private Integer rating;
    private String comment;
    private OffsetDateTime submittedAt;
    private String schemaVersion = "1.0.0";

    /*
     * SECTION: No-Args Constructor
     */
    public FeedbackResponse() {}

    /*
     * SECTION: All-Args Constructor
     */
    public FeedbackResponse(UUID id, String memberId, String providerName, Integer rating, String comment, OffsetDateTime submittedAt) {
        this.id = id;
        this.memberId = memberId;
        this.providerName = providerName;
        this.rating = rating;
        this.comment = comment;
        this.submittedAt = submittedAt;
    }

    /*
     * SECTION: Getter / Setter Methods
     */
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public OffsetDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(OffsetDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    /*
     * SECTION: Overrides for equals() and hashCode()
     */

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        FeedbackResponse that = (FeedbackResponse) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /*
     * SECTION: toString Override
     */
    @Override
    public String toString() {
        return "FeedbackResponse{" +
                "id=" + id +
                ", memberId='" + memberId + '\'' +
                ", providerName='" + providerName + '\'' +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", submittedAt=" + submittedAt +
                '}';
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }
}
