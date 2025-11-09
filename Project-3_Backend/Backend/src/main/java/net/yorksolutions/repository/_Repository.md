# Repository

[Spring Data JPA Tutorial](https://www.youtube.com/watch?v=dKK2dZVLFug&list=PLGRDMO4rOGcPtMMl1DD2cu3GBPZloHOS_)

Spring applications, particularly with Spring Boot and Spring Data JPA, use the *Entity* (a.k.a., *Model*), *Repository* and *Service* architecture.

A *Repository* is an `interface` that defines methods for performing CRUD operations on an *Entity*.

## Spring Data
Spring Data LDAP, Spring Data MongoDB, Spring Data JPA, Spring Data JDBC are all derived from Spring Data Commons.

```
Repository -> CrudRepository -> PaginingAndSortingRepository -> JpaRepository 
```

## Steps to Create and Use JPA Repository
1. Create a repository interface and extend `JpaRepository`
2. Add custom query methods to the created repository interface (as necessary)
3. Inject the repository interface to another component and use the implementation that is provided automatically by Spring Data JPA

### Step 01

```java
// Product - the JPA 'Entity'
// Integer - The data-type associated with the primary key
public interface ProductRepository extends JpaRepository<Product, Integer> {}
```

### Step 02

```java
public interface ProductRepository extends JpaRepository<Product, Integer> {
  // A Query Method or Finder Method
  Product findByName(String name);
}
```

**Note:**
- It is **not** required to annotate a *Repository* with `@Repository` if it `extends JpaRepository` because the `SimpleJpaRepository` class already denotes that annotation so any *Repository* will, thus, inherit it implicitly
- A class that implements each *Repository* is **not** necessary because the Repositories Entity gets all methods to perform CRUD operations from JpaRepository

### Step 03
This step is picked up with the *Service*, as such the notes for it will be over there.

## Repository Methods
Any custom *Repository* that is created, if `JpaRepository` is extended, will receive the methods from:
- JpaRepository
- PagingAndSortingRepository
- CrudRepository
  - **Note:** These methods are the ones that are most commonly used

Using the Reflection API, it is possible to pull all of the methods out of each *Interface*:

```java
Class<?> interfaceClass = CrudRepository.class;
Method[] methods = interfaceClass.getDeclaredMethods();
for (Method method : methods) {
  System.out.println("METHOD: " + method.getName());
}
```

### JpaRepository Methods
- deleteAllByIdInBatch
- saveAndFlush
- saveAllAndFlush
- deleteInBatch [**deprecated**]
- deleteAllInBatch [**multiple**]
- getOne [**deprecated**]
- getById [**deprecated**]
- getReferenceById
- findAll [**multiple**]
- flush

### PagingAndSortingRepository Methods
- findAll

### CrudRepository Methods
- findAllById
- saveAll
  - **Note:** Returns a list of Iterable objects
- findById
  - **Note:** This returns an *Optional* of type {{Entity}}
- existsById
- deleteById
- deleteAllById
- deleteAll
- deleteAll
- findAll
- count
- save
  - **Note:** This method will *persist* **or** *merge* the entity depending on whether the entity does/not exist in the database (i.e., it can handle both a “create” and an “update")
- delete

## What Can Be Added to Repository (DeepSeek)
In the Java Spring Framework, Entity-specific Repository interfaces typically extend one of Spring Data's repository interfaces, and the methods you declare depend on your needs. Here's a breakdown:

### Repository Method Approaches
#### Standard Repository Extensions
Most commonly, you extend these interfaces:

```java
public interface UserRepository extends JpaRepository<User, Long> {
  // No methods needed for basic CRUD operations
}

public interface ProductRepository extends CrudRepository<Product, Long> {
  // Basic CRUD operations provided automatically
}
```

#### Query Methods (Most Common Custom Approach)
Spring Data can derive queries from method names:

```java
public interface UserRepository extends JpaRepository<User, Long> {
  // Basic finders
  List<User> findByLastName(String lastName);
  Optional<User> findByEmail(String email);
  
  // Complex queries
  List<User> findByLastNameAndFirstName(String lastName, String firstName);
  List<User> findByAgeGreaterThan(int age);
  List<User> findByLastNameOrderByFirstNameAsc(String lastName);
  
  // Projections
  List<UserProjection> findProjectedByLastName(String lastName);
}
```

**Note:**
- Spring Data JPA will automatically generate method functionality *under the hood* **if** the method naming convention follows: `<{Action}><By><{Criteria}><{Operator}><{Condition}>`
  - Common prefixes:
    - `find…By`, `get…By`, `read…By`, `count…By`, `delete…By`, `exists…By`
  - EX: `List<User> findByLastName(String lastName)`
  - EX: `List<User> findByEmailIgnoreCase(String email)`

#### Custom Query Methods with @Query
For more complex queries:

```java
public interface UserRepository extends JpaRepository<User, Long> {
  @Query("SELECT u FROM User u WHERE u.email LIKE %:domain")
  List<User> findByEmailDomain(@Param("domain") String domain);
  
  @Query(
    "SELECT u.firstName, u.lastName FROM User u WHERE u.age > :minAge"
  )
  List<Object[]> findNamesByMinAge(@Param("minAge") int minAge);
  
  @Query(
    value = "SELECT * FROM users WHERE status = :status",
    nativeQuery = true
  )
  List<User> findByStatusNative(@Param("status") String status);
}
```

#### Custom Repository Methods
When you need complex implementation logic:

```java
// Main repository interface
public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {
  // Standard Spring Data methods
  List<User> findByActiveTrue();
}

// Custom interface
public interface CustomUserRepository {
  List<User> findUsersWithComplexBusinessLogic(String criteria);
}

// Implementation (name must follow convention: Impl suffix)
public class CustomUserRepositoryImpl implements CustomUserRepository {
  @PersistenceContext
  private EntityManager entityManager;
  
  @Override
  public List<User> findUsersWithComplexBusinessLogic(String criteria) {
    // Custom implementation logic
    String jpql = "SELECT u FROM User u WHERE ...";
    return entityManager.createQuery(jpql, User.class)
    .setParameter("criteria", criteria)
    .getResultList();
  }
}
```

#### No Methods Needed Scenario
For basic CRUD operations, you might not need any methods:

```java
public interface ProductRepository extends JpaRepository<Product, Long> {
  // All basic CRUD operations are inherited:
  // save(), findById(), findAll(), delete(), etc.
}
```

### Key Points
- No methods required for basic CRUD operations when extending `JpaRepository` or `CrudRepository`
- Query derivation automatically implements methods based on naming conventions
- `@Query` annotation for JPQL or native SQL queries
- Custom implementations for complex business logic that can't be expressed via query methods
- Return types can be flexible: `List<T>`, `Optional<T>`, `Page<T>`, `Stream<T>`, etc.

## Helpful Take-Aways
- IntelliJ IDEA - Hovering over the method name (e.g., `JpaRepository`) and clicking on the pencil (i.e., Jump to Source) enables you to preview the built-in methods of `JpaRepository`
  - **Note:** Use this to drill down through other interfaces that it extends
  - **Note:** The `SimpleJpaRepository` class `implements` `JpaRepository` as such it implements all interfaces that `JpaRepository` extends