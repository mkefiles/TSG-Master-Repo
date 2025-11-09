package net.yorksolutions.entity;

import java.util.UUID;

public class UserPlan {

    /**
     * This class is solely intended to serve as a binding-class
     * for the initial User-Plan creation. This is necessary because
     * the current 'API Design' does not allow for placing an ID
     * in the URL and there is no Get Member end-point. This will be
     * used in a manner where the Front-End receives a User ID upon
     * login, then maintains that User ID throughout the process at
     * which point it can communicate it back to the API (this end-point),
     * which allows the API to query the Members table for the
     * Member ID that lines up with the User ID that way the Enrollment
     * can be associated with the Member ID
     */

    /**
     * User-specific data-points
     */
    private UUID userId;

    /**
     * Plan-specific data-points
     */
    private UUID planId;

    /**
     * Generated All-Fields Constructor
     * Useful for creating instances with all fields
     */
    public UserPlan(UUID userId, UUID planId) {
        this.userId = userId;
        this.planId = planId;
    }

    /**
     * Generated Getter/Setter methods
     */
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getPlanId() {
        return planId;
    }

    public void setPlanId(UUID planId) {
        this.planId = planId;
    }
}
