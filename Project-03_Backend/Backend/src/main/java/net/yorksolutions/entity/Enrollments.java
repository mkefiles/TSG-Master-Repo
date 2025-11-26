package net.yorksolutions.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "enrollments")
public class Enrollments {

    /**
     * Define variables to line up with data in database
     */
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    @JsonBackReference
    private Members memberId;

    @ManyToOne
    @JoinColumn(name = "plan_id", referencedColumnName = "id")
    @JsonBackReference
    private Plans planId;

    @Column(name = "effective_start", nullable = false)
    private LocalDate effectiveStart;

    @Column(name = "effective_end")
    private LocalDate effectiveEnd;

    /**
     * Create a 'no-args' constructor
     * This enables JPA to create an instance through Reflection
     */
    public Enrollments() {
    }

    /**
     * Generated Required-Fields Constructor
     * Useful for creating instances where not all data-points are rec'd
     */
    public Enrollments(UUID id, Members memberId, Plans planId, LocalDate effectiveStart) {
        this.id = id;
        this.memberId = memberId;
        this.planId = planId;
        this.effectiveStart = effectiveStart;
    }

    /**
     * Generated All-Fields Constructor
     * Useful for creating instances with all fields
     */
    public Enrollments(
            UUID id, Members memberId, Plans planId,
            LocalDate effectiveStart, LocalDate effectiveEnd
    ) {
        this.id = id;
        this.memberId = memberId;
        this.planId = planId;
        this.effectiveStart = effectiveStart;
        this.effectiveEnd = effectiveEnd;
    }

    /**
     * Generated Getter/Setter methods
     */
    public LocalDate getEffectiveEnd() {
        return effectiveEnd;
    }

    public void setEffectiveEnd(LocalDate effective_end) {
        this.effectiveEnd = effective_end;
    }

    public LocalDate getEffectiveStart() {
        return effectiveStart;
    }

    public void setEffectiveStart(LocalDate effective_start) {
        this.effectiveStart = effective_start;
    }

    public Plans getPlanId() {
        return planId;
    }

    public void setPlanId(Plans plan_id) {
        this.planId = plan_id;
    }

    public Members getMemberId() {
        return memberId;
    }

    public void setMemberId(Members member_id) {
        this.memberId = member_id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Generated override for toString()
     */
    @Override
    public String toString() {
        return "Enrollments{" +
                "id=" + id +
                ", plan_id=" + planId +
                '}';
    }

    /**
     * Generated overrides for equals() and hashCode()
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Enrollments that = (Enrollments) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
