# Spring

## Good-to-Knows

- Spring Data / Hibernate does not appear to handle making relationship modifications (e.g., from One-to-One to Many-to-One)
  - Either set the configuration to drop the tables on every shut-off **or** make the modification in PostgreSQL shell
- Spring Data JPA relies heavily on Java naming conventions (i.e., `camelCase`) so it is best to have all of your *Entity* variable names match that
  - **Note:** This can cause hard-to-debug compile-time errors

```java
// Example: DB column uses `snake_case` whereas Entity column
// ... uses `camelCase`
@Column(name = "municipal_id", nullable = false)
private Integer municipalId;
```

- In the event where the `ResponseEntity` is returning too much data due to foreign-key relationships, use the `@JsonBackReference` annotation above the property-name in the Entity where the relationship-annotation (e.g., `@OneToOne`) is specified
- When using custom *update* queries, use the `@Transactional` annotation (to avoid errors)

## Annotations
- `@Autowired` - used for *Dependency Injection*
  - Automatically injects required dependencies without the need for manual configuration
- `@CrossOrigin` - used to specific if/what other domains are able to pull data from the domain that is hosting the Spring instance (see below)

## CORS (Allow Dev Server Origin)
Cross-Origin Resource Sharing is an HTTP-header-based mechanism that allows a server to indicate any *origins* (domain, scheme or port). Basically, can *domain-x* grab resources/data from *domain-y*.

Spring has a built-in annotation `@CrossOrigin` that enables CORs for specific *Controllers* or *Methods*.

```java
@RestController
// NOTE: Allow requests from your Front-End Dev Server (e.g., React)
@CrossOrigin(origins = "http://localhost:3000")
public class MyController {
  
  @GetMapping("/api/data")
  public String getData() {
    return "Hello from Spring Boot!";
  }
}
```

To allow for more of a dynamic usage, it is possible to set this in the *application.properties* file:

```properties
# Define the Client-Side Access URL (for CORS Dev-Server Origin)
## The default port for React is 3000
client.url=http://localhost:3000
```

```java
// -- snippet --
@CrossOrigin(origins = "${client.url}")
public class HealthcareSiteController {
	// -- snippet --
}
```

## Errors (and Solutions)

### ERROR: Relation X.Y Does Not Exist
In the *Entity* file, do **not** specify *schema* in the `@Table` annotation (e.g., `@Table(name = "plans")`).

When using PostgreSQL, it will append the database name and try to query like this:

```sql
-- Throws an error in PostgreSQL
SELECT * FROM healthcare_site.plans;

-- Correct
SELECT * FROM plans;
```

### Row Was Update or Deleted...
The Spring Data JPA error, “row was updated or deleted by another transaction (or unsaved-value mapping was incorrect)” is, apparently, due to an issue with Hibernate 6.6+. When an Entity has an `@GeneratedValue` ID, Hibernate expects the ID to be `null` when saving a new Entity. If you manually set an ID, Hibernate thinks the entity already exists and tries to update it — if it does not find it, it will throw an *OptimisticLockException*. To avoid this issue, make sure that the ID is **not** set when saving *new* entities.

[Resource](https://www.reddit.com/r/javahelp/comments/1kflxat/row_was_updated_or_deleted_by_another_transaction/)

I encountered this issue when I was trying to get the ID of “User” to add that ID as a *Foreign Key* to “Member”. I created a variable that retrieved that newly created UUID then added it to the Member Object/Entity prior to saving the Member to the database. The solution is, unfortunately, a five-step process:

1. Add the User
2. Fetch the ID from what `.save()` returns
3. Add the Member
4. Locate the Member just added
5. Update the Member for the ID

### Using UUIDs (with Partial Data Updates)
As a continuation of the prior *error*, Spring Data JPA and PostgreSQL may, especially with Custom Queries, throw data mis-match errors. What happens is that the Custom Query is NOT passing the parameter as a UUID so it must be *cast*:

```java
// --snippet from Repository--
@Modifying
@Query(
  value = "UPDATE members SET user_id = CAST(:user_id AS UUID) WHERE id = CAST(:id AS UUID)", nativeQuery = true
)
public void updateMembers(
  @Param(value = "user_id") UUID user_id, @Param(value = "id") UUID id
);
```

**Note:**
- The `nativeQuery` attribute will ensure that the query is being passed in the dialect-specific manner
  - This *can* cause issues when trying to change the underlying database being used

### NoClassDefFoundError on Bouncy Castle
The Spring Security dependency…

```xml
<dependency>
  <groupId>org.springframework.security</groupId>
  <artifactId>spring-security-crypto</artifactId>
  <version>6.5.5</version>
</dependency>
```

… does **not** include all necessary BouncyCastle dependencies (what is required to use Argon2 encryption…) go figure… so the following dependency is also necessary:

```xml
<dependency>
  <groupId>org.bouncycastle</groupId>
  <artifactId>bcprov-jdk18on</artifactId>
  <version>1.82</version>
</dependency>
```
