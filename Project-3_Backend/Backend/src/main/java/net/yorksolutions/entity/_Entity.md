# Entity

[Spring Data JPA Tutorial](https://www.youtube.com/watch?v=dKK2dZVLFug&list=PLGRDMO4rOGcPtMMl1DD2cu3GBPZloHOS_)

Spring applications, particularly with Spring Boot and Spring Data JPA, use the *Entity* (a.k.a., *Model*), *Repository* and *Service* architecture.

An *Entity* is a *Plain Ol’ Java Object* that represents a table in a relational database.

## Terminology
- Lazy Loading - only load the necessary data when it is first needed

## Annotations and Structure
- Annotations:
  - `@Column()` is used to map the column when the variable-name does not match
  - `@Id` simply specifies that the following variable is the table ID
  - `@GeneratedValue()` is used to tell JPA that the value should be auto-generated at creation
- For each *Entity*, the following is necessary for the Jackson Objectmapper to work
  - A default no-args constructor (empty)
    - **Note:** This is necessary because JPA uses it to create instances of the entity class using Reflection
  - An all-args constructor
  - An override for `toString()`, `equals()` and `hashCode()`
  - Applicable *getter* and *setter* methods

**Note:**
- There is an annotation for `@Data`, `@AllArgsConstructor` and `@NoArgsConstructor` that can be appended to each prior to the `@Entity` annotation, however this requires the Lombok dependency
  - `@Data` encapsulates the `@Getter`, `@Setter`, `@RequiredArgsConstructor`, `@ToString` and `@EqualsAndHashCode` annotations provided by Lombok
- Per [Baeldung](https://www.baeldung.com/intro-to-project-lombok), “in general [you] should avoid using Lombok to generate the `equals()` and `hashCode()` methods for JPA entities"

## Primary Key Generation Strategies

`@GeneratedValue()`

```java
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private UUID id;
```

- `GenerationType`
  - `.AUTO` - default; lets the persistence provider choose the generation strategy
  - Hibernate, when used as the *persistence provider* will select a generation strategy based on the database-specific dialect
    - Most popular databases are placed with `GenerationType.SEQUENCE`
  - `.IDENTITY` - relies on the database auto-increment column
    - From a database perspective, this is the most efficient method (assuming that the data-type handles *auto-increment*)
    - **Note:** UUID does not have auto-increment
    - This process is not optimal for batch JDBC operations
  - `.SEQUENCE` - most commonly used; used to generate primary key values and uses a database sequence to generate unique values
    - Requires additional select statements to get the next value from the database sequence (this has little to no impact on most applications)
    - The `@SequenceGenerator` annotation lets you define the name of the generator, the name and schema of the database sequence and the allocation size of the sequence (**look into this when necessary**)
  - `.TABLE` - rarely used; requires a *pessimistic lock* that puts all transactions into a sequential order
    - This will, likely, slow down your application and, thus, should be avoided

**Note:**
- **IF** using a *UUID* data-type **AND** a Hibernate version >= 6.2, then it is best to use the `@UuidGenerator` annotation (in lieu of `@GeneratedValue`)
  - It is possible to specify how the value is generated (`@UuidGenerator (style = UuidGenerator.Style.TIME)`) :
    - `.TIME` - generate time-based UUID
    - `.RANDOM` - generate UUID based on random numbers
    - `.AUTO` - default; same as `RANDOM`

## @CreationTimestamp
Using `@CreationTimestamp` and `@UpdateTimestamp` can help when one needs to track the creation date of an entity.

**Note:**
- These work with the Java `ZonedDateTime` data-type

## Constructors
Java Spring needs different types of constructors depending on the use-case and the framework being used.

- No-Argument Constructor
  - This is **required** by JPA/Hibernate
- All-Argument Constructor
  - Useful for creating instances with all fields
- Required Fields Constructor
  - For creating instances without the auto-generated ID
  - **Best Practice:** Use a constructor for required fields to ensure valid object creation
- Copy Constructor
  - For creating copies of existing entities
- Builder Pattern Constructor
  - Often used with `@Builder` from Lombok
  - **Best Practice:** Use Builder Pattern for complex objects with many optional fields

```java
// Example with multiple constructors

@Entity
  public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;

    // No Argument Constructor
    protected Product() {}

    // Required-Fields Constructor
    public Product(String name, String description, BigDecimal price) {
      this.name = name;
      this.description = description;
      this.price = price;
    }

    // All-Fields Constructor
    public Product(Long id, String name, String description, BigDecimal price) {
      this.id = id;
      this.name = name;
      this.description = description;
      this.price = price;
    }

    // Copy Constructor
    public Product(Product other) {
      this.id = other.id;
      this.name = other.name;
      this.description = other.description;
      this.price = other.price;
    }
  } 
```

> My general takeaway, with this, is to default to have a No-Arg Constructor, an All-Arg Constructor and, as necessary based on the Data Model, a Required-Arg Constructor (if all arguments are required then a Required-Arg Constructor would be redundant)

## equals() and hashCode()
In Java, when an *Object* is created, an integer value, typically representative of the internal address in memory, is created as the “hash code” assigned to it. So...

> If two objects are "equal", according to the `equals()` method, then calling `hashCode()` on each MUST produce the same integer result.

That is a fundamental rule of Java and is one that Spring, and other code, relies on to work correctly.

So *when* is it necessary to override the `equals()` and `hashCode()` methods?

- When storing an Object in an `@Entity` class
  - This is due to Hibernate relying on `hashCode()` when it fetches Objects, from the database, and caches them for faster retrieval
  - Rule of thumb, **always** override for `@Entity` classes
  - These should be based on a **unique** and **immutable** key (i.e., the primary key… in most cases)
    - EX: Two `User` entities are the “same” if they have the same `id` (even if they are different objects in memory)
- When using your Object as a *key* in a `HashMap` or `HashSet`
  - Spring uses `HashMap` and `HashSet`, internally, everywhere. If you use your own Object as a key, you **must** override these methods
- When using Caching (i.e., `@Cacheable`)
  - When storing the results of method-calls, the framework needs to create a unique key for each method-call to see if the result is already in the cache

**Note:**
- There are “quite a few pitfalls” when typing these by hand so it is best-practice to let your IDE generate the methods (see [Baeldung](https://baeldung.com/java-equals-hashcode-contracts) for additional information)
  - Alternatively you could use the Lombok dependency to handle this
  - IntelliJ IDEA - Right-click on the code and click Generate
    - I used “IntelliJ Default” and the `getClass()` comparison expression then selected to only compare on the `id` because the primary key is “almost always [the best candidate]"

### Cheat Sheet
- Scenario [Should Override?; Why?]
  - `@Entity` class
    - Yes… **always**
    - Critical for JPA/Hibernate caching and correct behavior
  - Object as a *key* in `HashMap`
    - Yes
    - Required for the `Map` to find your keys correctly
  - Object in a `HashSet`
    - Yes
    - A `HashSet` is just a `HashMap` internally
  - Using `@Cacheable`
    - Yes (if parameters are custom objects)
    - Needed for correct cache key generation
  - `@Service` and `@Controller` Beans
    - No
    - Spring manages these by type/name, not hashing
  - Simple DTOs/POJOs
    - No (Usually)
    - Only if you need to compare them logically or use them in hashed collections

### The Golden Rule

> If you ever think, “I want these two different objects to be considered the same based on their data”, you need to override `equals()`. And if you override `equals()`, you **must** override `hashCode()` to obey Java’s contract

## toString()
It is possible to auto-generate the `toString()` override, however just do **not** include any *lazy fields*.

Lazy fields are, typically, as follows:
- Collection-based relationships (`@OneToMany`, `@ManyToMany`)
- Explicitly marked `@OneToOne` or `@ManyToOne` relationships with `fetch = FetchType.LAZY`
- Less commonly, basic attributes marked with `@Basic(fetch = FetchType.LAZY)` (often requiring specific JPA provider configurations)