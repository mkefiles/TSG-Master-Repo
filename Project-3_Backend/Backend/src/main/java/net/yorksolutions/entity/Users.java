package net.yorksolutions.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
public class Users {
    /**
     * Create an Enum for the possible values that `role` can have
     * Note that an Enum is a special class...not an object
     */
    public enum Role {
        MEMBER,
        ADMIN
    }

    /**
     * Define variables to line up with data in database
     */
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    /**
     * Create a 'no-args' constructor
     * This enables JPA to create an instance through Reflection
     */
    public Users() {
    }

    /**
     * Generated All-Fields Constructor
     * Useful for creating instances with all fields
     * Note: This also serves as a Required Fields Constructor because, per
     * ... the Data Model, all columns are "NOT NULL" (i.e., they are all
     * ... necessary/"required" fields)
     */
    public Users(UUID id, String email, String passwordHash, Role role, ZonedDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Generated override for toString()
     */
    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }

    /**
     * Generated overrides for equals() and hashCode()
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Users users = (Users) o;
        return id.equals(users.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
