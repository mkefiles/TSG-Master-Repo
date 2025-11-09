package net.yorksolutions.entity;


import java.time.LocalDate;

public class MemberUser {

    /**
     * This class is solely intended to serve as a binding-class
     * for the initial Member-User registration. This is necessary
     * because the current 'API Design' has one end-point that is used
     * to collect the information for the Users DB and the Members DB
     * upon initial registration. The other option of using @RequestBody
     * and mapping to a Map proves to be unreliable because the data will
     * be a mixture of data-types.
     *
     * Given that this will not be mapped to the database, it does not need
     * any Spring annotations.
     */

    /**
     * Member-specific data-points
     */
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    /**
     * User-specific data-points
     */
    private String email;
    private String passwordHash;

    /**
     * Generated All-Fields Constructor
     * Useful for creating instances with all fields
     */
    public MemberUser(
            String firstName, String lastName, LocalDate dateOfBirth,
            String email, String passwordHash
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    /**
     * Generated Getter/Setter methods
     */
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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
}
