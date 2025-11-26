package net.yorksolutions.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "claims")
public class Claims {
    /**
     * Create an Enum for the possible values that `status` can have
     * Note that an Enum is a special class...not an object
     */
    public enum Status {
        SUBMITTED,
        IN_REVIEW,
        APPROVED,
        DENIED
    }

    /**
     * Define variables to line up with data in database
     */
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "enrollment_id", referencedColumnName = "id")
    private Enrollments enrollmentId;

    @Column(nullable = false)
    private Date dos;

    @Column(name = "provider_name", nullable = false)
    private String providerName;

    @Column(name = "amount_cents", nullable = false)
    private Integer amountCents;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    /**
     * Create a 'no-args' constructor
     * This enables JPA to create an instance through Reflection
     */
    public Claims() {
    }

    /**
     * Generated Required-Fields Constructor
     * Useful for creating instances where not all data-points are rec'd
     */
    public Claims(
            UUID id, Enrollments enrollmentId, Date dos, String providerName,
            Integer amountCents, Status status, ZonedDateTime createdAt, ZonedDateTime updatedAt
    ) {
        this.id = id;
        this.enrollmentId = enrollmentId;
        this.dos = dos;
        this.providerName = providerName;
        this.amountCents = amountCents;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Generated All-Fields Constructor
     * Useful for creating instances with all fields
     */
    public Claims(
            UUID id, Enrollments enrollmentId, Date dos, String providerName,
            Integer amountCents, String description, Status status,
            ZonedDateTime createdAt, ZonedDateTime updatedAt
    ) {
        this.id = id;
        this.enrollmentId = enrollmentId;
        this.dos = dos;
        this.providerName = providerName;
        this.amountCents = amountCents;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Generated Getter/Setter methods
     */
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Enrollments getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Enrollments enrollment_id) {
        this.enrollmentId = enrollment_id;
    }

    public Date getDos() {
        return dos;
    }

    public void setDos(Date dos) {
        this.dos = dos;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String provider_name) {
        this.providerName = provider_name;
    }

    public Integer getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(Integer amount_cents) {
        this.amountCents = amount_cents;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime created_at) {
        this.createdAt = created_at;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updated_at) {
        this.updatedAt = updated_at;
    }

    /**
     * Generated override for toString()
     */
    @Override
    public String toString() {
        return "Claims{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", amount_cents=" + amountCents +
                '}';
    }

    /**
     * Generated overrides for equals() and hashCode()
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Claims claims = (Claims) o;
        return id.equals(claims.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
