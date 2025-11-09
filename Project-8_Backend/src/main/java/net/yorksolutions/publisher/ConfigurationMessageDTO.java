package net.yorksolutions.publisher;

import java.time.Instant;
import java.util.UUID;

public class ConfigurationMessageDTO {

    /**
     * SECTION: DTO / POJO Variables
     */
    private UUID eventId;
    private Instant occurredAt;
    private String employerId;
    private String planYear;
    private Double monthlyAllowance;

    /**
     * SECTION: No-Args Constructor
     */
    public ConfigurationMessageDTO() {}

    /**
     * SECTION: Required-Args Constructor
     */
    public ConfigurationMessageDTO(String employerId, String planYear, Double monthlyAllowance) {
        this.employerId = employerId;
        this.planYear = planYear;
        this.monthlyAllowance = monthlyAllowance;
    }

    /**
     * SECTION: All-Args Constructor
     */
    public ConfigurationMessageDTO(
            UUID eventId, Instant occurredAt, String employerId,
            String planYear, Double monthlyAllowance
    ) {
        this.eventId = eventId;
        this.occurredAt = occurredAt;
        this.employerId = employerId;
        this.planYear = planYear;
        this.monthlyAllowance = monthlyAllowance;
    }

    /**
     * SECTION: Getter and Setter methods
     */
    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(Instant occurredAt) {
        this.occurredAt = occurredAt;
    }

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public String getPlanYear() {
        return planYear;
    }

    public void setPlanYear(String planYear) {
        this.planYear = planYear;
    }

    public Double getMonthlyAllowance() {
        return monthlyAllowance;
    }

    public void setMonthlyAllowance(Double monthlyAllowance) {
        this.monthlyAllowance = monthlyAllowance;
    }

    /**
     * SECTION: toString Override
     */
    @Override
    public String toString() {
        return "ConfigurationMessageDTO{" +
                "eventId=" + eventId +
                ", occurredAt=" + occurredAt +
                ", employerId='" + employerId + '\'' +
                ", planYear='" + planYear + '\'' +
                ", monthlyAllowance=" + monthlyAllowance +
                '}';
    }
}
