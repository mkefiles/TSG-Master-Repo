package net.yorksolutions.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "members")
public class Members {

    /**
     * Define variables to line up with data in database
     */
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    // Foreign-key of `user_id` in `members` table
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users userId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "dob", nullable = false)
    private LocalDate dateOfBirth;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    /**
     * Create a 'no-args' constructor
     * This enables JPA to create an instance through Reflection
     */
    public Members() {
    }

    /**
     * Generated All-Fields Constructor
     * Useful for creating instances with all fields
     * Note: This also serves as a Required Fields Constructor because, per
     * ... the Data Model, all columns are "NOT NULL" (i.e., they are all
     * ... necessary/"required" fields)
     */
    public Members(
            UUID id, Users userId, String firstName, String lastName,
            LocalDate dateOfBirth, ZonedDateTime createdAt
    ) {
        this.id = id;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
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

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users user_id) {
        this.userId = user_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String first_name) {
        this.firstName = first_name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String last_name) {
        this.lastName = last_name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate date_of_birth) {
        this.dateOfBirth = date_of_birth;
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
        return "Members{" +
                "id=" + id +
                ", first_name='" + firstName + '\'' +
                ", last_name='" + lastName + '\'' +
                '}';
    }

    /**
     * Generated overrides for equals() and hashCode()
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Members members = (Members) o;
        return id.equals(members.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
