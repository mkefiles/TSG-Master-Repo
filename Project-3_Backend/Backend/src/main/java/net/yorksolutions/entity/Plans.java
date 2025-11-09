package net.yorksolutions.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "plans")
public class Plans {

    /**
     * Define variables to line up with data in database
     */
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(name = "premium_monthly", nullable = false)
    private Double premiumMonthly;

    @Column(nullable = false)
    private Double deductible;

    @Column(name = "coinsurance_percent", nullable = false)
    private Integer coinsurancePercent;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    /**
     * Create a 'no-args' constructor
     * This enables JPA to create an instance through Reflection
     */
    public Plans() {
    }

    /**
     * Generated All-Fields Constructor
     * Useful for creating instances with all fields
     * Note: This also serves as a Required Fields Constructor because, per
     * ... the Data Model, all columns are "NOT NULL" (i.e., they are all
     * ... necessary/"required" fields)
     */
    public Plans(
            UUID id, String code, String name, Double premiumMonthly,
            Double deductible, Integer coinsurancePercent, ZonedDateTime createdAt
    ) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.premiumMonthly = premiumMonthly;
        this.deductible = deductible;
        this.coinsurancePercent = coinsurancePercent;
        this.createdAt = createdAt;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPremiumMonthly() {
        return premiumMonthly;
    }

    public void setPremiumMonthly(Double premium_monthly) {
        this.premiumMonthly = premium_monthly;
    }

    public Double getDeductible() {
        return deductible;
    }

    public void setDeductible(Double deductible) {
        this.deductible = deductible;
    }

    public Integer getCoinsurancePercent() {
        return coinsurancePercent;
    }

    public void setCoinsurancePercent(Integer coinsurance_percent) {
        this.coinsurancePercent = coinsurance_percent;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime created_at) {
        this.createdAt = created_at;
    }

    /**
     * Generated override for toString()
     */
    @Override
    public String toString() {
        return "Plans{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * Generated overrides for equals() and hashCode()
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Plans plans = (Plans) o;
        return id.equals(plans.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
