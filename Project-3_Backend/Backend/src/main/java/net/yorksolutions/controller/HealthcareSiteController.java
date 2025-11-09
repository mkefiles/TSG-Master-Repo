package net.yorksolutions.controller;

import net.yorksolutions.entity.*;
import net.yorksolutions.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

// Step 000: Annotation-based Configuration
// Step 01A: Identify a Controller (Annotation)
// ... @RestController removes the need for method-annotation @ResponseBody
@RestController // Controller "Bean"

// DESC: Allow Dev-Server Origin (CORS)
// NOTE: I have this defined in the 'application.properties'
@CrossOrigin(origins = "${client.url}")
public class HealthcareSiteController {
    /**
     * Initialize all applicable Services
     */
    private final ClaimsService claimsService;
    private final EnrollmentsService enrollmentsService;
    private final MembersService membersService;
    private final PlansService plansService;
    private final UsersService usersService;


    public HealthcareSiteController(
            ClaimsService claimsService, EnrollmentsService enrollmentsService,
            MembersService membersService, PlansService plansService,
            UsersService usersService
            ) {
        this.claimsService = claimsService;
        this.enrollmentsService = enrollmentsService;
        this.membersService = membersService;
        this.plansService = plansService;
        this.usersService = usersService;
    }

    /**
     * DESC: Create an instance of the Argon2 Encoder
     */
    private final Argon2PasswordEncoder encoder =
            new Argon2PasswordEncoder(32, 64, 1, 65536, 2);

    /**
     * PT01 - Handler to Create new Plan (POST) [OPTIONAL]
     * <p>
     * End-point: localhost:8080/api/v1/plans
     * <p>
     * End-point used to Create new plans in the database.
     * This will return the plan information (with a 200 status)
     * if successful, otherwise it returns an error-code of 400
     * @param newPlan JSON payload that consists of a 'code',
     *                a 'name', the 'premium_monthly', the
     *                'deductible' and the 'coinsurance_percent'
     * @return HTTP status code and, if successful, the plan info
     * otherwise it returns an empty body
     */
    @PostMapping("/api/v1/plans")
    public ResponseEntity<Plans> createNewPlan(@RequestBody Plans newPlan) {
        // DESC: Add plan to database
        int returnedStatusCode = plansService.createNewPlan(newPlan);

        // DESC: Handle returned code
        switch (returnedStatusCode) {
            case 200:
                // DESC: A successful add
                // NOTE: .body() will return a JSON repr. of the Plan object
                return ResponseEntity
                        .status(200)
                        .body(newPlan);
            default:
                // DESC: A failed add
                // NOTE: .build() will return an empty body
                return ResponseEntity
                        .status(400)
                        .build();
        }
    }

    /**
     * PT02 - Handler for Reading all Plans (GET)
     * <p>
     * End-point: localhost:8080/api/v1/plans
     * <p>
     * End-point used to Read all plans from the database.
     * This will always return a success code of 200 along
     * with the list of plans
     * @return HTTP status code and plans (an empty list will
     * be returned in the event where there are no plans)
     */
    @GetMapping("/api/v1/plans")
    public ResponseEntity<List<Plans>> readAllPlans() {
        return ResponseEntity
                .status(200)
                .body(plansService.readAllPlans());
    }

    /**
     * PT03 - Handler for Reading a Plan by ID (GET)
     * <p>
     * End-point: localhost:8080/api/v1/plans/{id}
     * <p>
     * End-point used to Read a plan, by ID, from the database.
     * This will always return a 200 code, however it will either
     * return a body with an empty list or the first plan that matches
     * the provided ID.
     * @param id the ID of the Plan as passed by the URL
     * @return return a 200 status code and the body, which will either
     * be the matching plan or empty
     */
    @GetMapping("/api/v1/plans/{id}")
    public ResponseEntity<Plans> readPlanById(@PathVariable String id) {
        // DESC: Convert String parameter to UUID
        UUID uuid = UUID.fromString(id);

        // DESC: Return Plan by ID
        // NOTE: This will always have a response of 200
        List<Plans> results = plansService.readPlanById(uuid);

        // DESC: Determine if the `results` List is empty
        if (results.isEmpty()) {
            // DESC: List is empty; Return an empty body
            return ResponseEntity
                    .status(200)
                    .build();
        } else {
            // DESC: List contains result(s); Return result(s)
            return ResponseEntity
                    .status(200)
                    .body(results.getFirst());
        }
    }

    /**
     * PT04 - Handler for Creating a new Member-User (POST)
     * <p>
     * End-point: localhost:8080/api/v1/auth/register
     * <p>
     * Given the project 'Data Model' and 'API Design' project-structure,
     * there is not supposed to be any separate Front-End that handles
     * adding/modifying member data and there is only one end-point for
     * registration so this end-point uses a 'binding-class' of MemberUser,
     * which is intentionally NOT an 'Entity'. MemberUser grabs all of the
     * provided information passed to it then parses it to the applicable
     * table (i.e., Users and Members)
     * @param memberUser JSON payload that consists of a 'first_name', a
     *                   'last_name', a 'date_of_birth', an 'email' and
     *                   a plain-text 'password_hash'
     * @return IF successful, then return the User ID and a status code of
     * 200. IF fail due to duplicate username, return 409 (conflict). IF fail
     * due to any other reason, return 400 (client error)
     */
    @PostMapping("/api/v1/auth/register")
    public ResponseEntity<Users> createNewMemberUser(@RequestBody MemberUser memberUser) {
        // DESC: Parse `MemberUser` into a User instance
        Users newUser = new Users();
        newUser.setEmail(memberUser.getEmail());
        newUser.setPasswordHash(encoder.encode(memberUser.getPasswordHash()));
        newUser.setRole(Users.Role.MEMBER);

        // DESC: Parse `MemberUser` into a Member instance
        Members newMember = new Members();
        newMember.setFirstName(memberUser.getFirstName());
        newMember.setLastName(memberUser.getLastName());
        newMember.setDateOfBirth(memberUser.getDateOfBirth());

        // DESC: Call the User-service (for logic)
        int returnedStatusCode = usersService.createNewMemberUser(newUser, newMember);

        // DESC: Determine 'return'
        switch (returnedStatusCode) {
            case 200:
                // DESC: A successful add
                // NOTE: .body() will return a JSON repr. of the User object
                return ResponseEntity
                        .status(200)
                        .body(newUser);
            case 409:
                // DESC: A failed registration (due to duplicate email)
                // NOTE: .build() will return an empty body
                return ResponseEntity
                        .status(409)
                        .build();
            default:
                // DESC: A failed registration (returns 400 for all other issues)
                // NOTE: .build() will return an empty body
                return ResponseEntity
                        .status(400)
                        .build();
        }
    }

    /**
     * PT05 - Handler for User-login (POST)
     * <p>
     * End-point: localhost:8080/api/v1/auth/login
     * <p>
     * End-point used for both Member- and Admin-User login. The project-logic
     * did not specify a need for an Admin login, however given that there are
     * two distinct roles and only one login, this handles both. Also, based on
     * research, a JWT should NOT contain "what a user can do" only "who the
     * user is" AND an API should not handle re-directing. That said, this returns
     * both the JWT as well as the users role that way the Front-End can properly
     * route the next step (e.g., an auto-redirect to the appropriate dashboard)
     * @param users JSON payload that consists of an 'email' and the plain-text
     *              'password_hash' for the user
     * @return a 30-minute JWT for further site usage as well as the users role
     */
    @PostMapping("/api/v1/auth/login")
    public Map<String, Object> login(@RequestBody Users users) {
        // DESC: Create a 'Map' to return multiple items to the body
        // NOTE: A JWT is, by default, returned in the response-body
        // ... as such I want to return the token AND the users role
        // ... that way the front-end can appropriately route the user
        Map<String, Object> response = new HashMap<>();

        // DESC: Return a String that contains a JWT
        // NOTE: This will, by default, return a HTTP 200
        String token = usersService.verifyUserCredentials(users);

        // DESC: Create a temp-user to hold information retrieved
        // ... from the database
        // NOTE: This enables React to query database for User ID
        // ... to get Member ID without another end-point as the project
        // ... did not specify an end-point that would help for that
        Users tempUser = usersService.getUserRoleAndId(users);

        // DESC: Add necessary elements to Map
        response.put("token", token);
        response.put("user_id", tempUser.getId());
        response.put("role", tempUser.getRole());

        return response;
    }

    /**
     * PT06 - Handler for Creating a new Admin-User (POST) [OPTIONAL]
     * <p>
     * End-point: localhost:8080/api/v1/auth/new_admin
     * <p>
     * End-point for Creating a new admin-user in the database.
     * Given that this is not a requirement set-forth by the project,
     * there is no additional logic for error-handling (e.g., confirming
     * password length, confirming proper email, etc.). This would,
     * likely, be commented out for Production. Having the end-point was
     * solely to ensure that the database had matching information (and
     * styling) for both Member- and Admin-User information (i.e., no
     * manual creation of an encrypted password, a UUID or a creation-date)
     * @param newUser JSON payload that consists of an 'email' and a plain-
     *                text 'password_hash'
     * @return status code (should always be 200) and the new users
     * information
     */
    @PostMapping("/api/v1/auth/new_admin")
    public ResponseEntity<Users> createNewAdminUser(@RequestBody Users newUser) {
        // DESC: Manually set the role (it is not provided by end-user)
        newUser.setRole(Users.Role.ADMIN);

        // DESC: Encrypt the provided plain-text password prior to
        // ... persisting to the database
        newUser.setPasswordHash(encoder.encode(newUser.getPasswordHash()));

        // DESC: Steps necessary to persist data to database
        int returnedStatusCode = usersService.createNewAdminUser(newUser);

        return ResponseEntity
                .status(returnedStatusCode)
                .body(newUser);
    }

    /**
     * PT07 - Handler for Creating a new Enrollment (POST)
     * <p>
     * End-point: localhost:8080/api/v1/me/enrollments
     * <p>
     * End-point for Creating a new Enrollment in the database. Note
     * that ANY enrollment will, simultaneously, end any other active
     * enrollment that the member has.
     * <p>
     * Note that this logic is NOT built to handle an event where the
     * member has multiple enrollments hanging around. Maybe that could be
     * an additional logic built out that periodically scans a database
     * and removes any inactive enrollments after a pre-determined period
     * of time
     * <p>
     * Note that the project mentions having an effectiveStart passed, however
     * the Front-End does not depict choosing a start date so, for simplicity,
     * this will assume that the effective start date is the date the button
     * was clicked AND that any current enrollment will also end that day as
     * well (i.e., an immediate enrollment/unenroll)
     * <p>
     * The `@PreAuthorize()` annotation ensures that ONLY a User who
     * has a 'MEMBER' role can access this end-point
     * @param userPlan JSON payload that consists of a 'user_id' and a
     *                 'plan_id' (note: there is a 'code' column in the
     *                 Plans table, however the project-structue did not
     *                 link anything to it/use it whatsoever (?))
     * @return a representation of the entity that was just created
     * as this is RESTful 'best practice' (and the 200 status-code)
     * otherwise, return 400 (client error)
     */
    @PreAuthorize("hasRole('MEMBER')")
    @PostMapping("/api/v1/me/enrollments")
    public ResponseEntity<Enrollments> createNewEnrollment(
            @RequestBody UserPlan userPlan
    ) {
        // DESC: Parse 'UserPlan' into a User instance
        Users tempUser = new Users();
        tempUser.setId(userPlan.getUserId());

        // DESC: Parse 'UserPlan' into a Plan instance
        Plans tempPlan = new Plans();
        tempPlan.setId(userPlan.getPlanId());

        // DESC: Call the Enrollments service (for logic)
        Enrollments enrollments = enrollmentsService.createNewEnrollment(tempUser, tempPlan);

        // DESC: Determine if enrollment was successful
        // NOTE: If the Member ID is null, then that suggests the
        // ... database work was unsuccessful/incomplete
        int returnedStatusCode = (Objects.isNull(enrollments.getMemberId())) ? 400 : 200;

        // TODO: Fix how this is dumping all information incl. User credentials
        // ... maybe just use a Map (like above) to control the output
        switch (returnedStatusCode) {
            case 200:
                return ResponseEntity
                        .status(200)
                        .body(enrollments);
            default:
                return ResponseEntity
                        .status(400)
                        .build();
        }
    }

    // TODO: ENROLLMENTS (MEMBER-SCOPED)
    // -- GET - /me/enrollments/active — current enrollment.

    // TODO: CLAIMS
    // -- GET - /me/claims — list my claims
    // -- POST - /me/claims — submit claim
    // -- GET - /me/claims/{id} — claim details

    // TODO: ADMIN
    // -- GET - /admin/claims?status=SUBMITTED — queue
    // -- PATCH - /admin/claims/{id} — update status (IN_REVIEW → APPROVED|DENIED)






    /*************************************************************
     * DELETE THE FOLLOWING CODE: DASHBOARD HANDLED BY FRONT-END *
     *************************************************************/
//    @PreAuthorize("hasRole('MEMBER')")
//    @GetMapping("/api/v1/me/dashboard")
//    public String getMemberDashboard(Authentication authentication) {
//        authentication = SecurityContextHolder.getContext().getAuthentication();
//        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
//            System.out.println(grantedAuthority.getAuthority());
//        }
//
//        System.out.println("AUTHENTICATION: " + authentication);
//        return "MEMBER DASHBOARD";
//    }
//
//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping("/api/v1/admin/dashboard")
//    public String getAdminDashboard(Authentication authentication) {
//        return "ADMIN DASHBOARD";
//    }
}