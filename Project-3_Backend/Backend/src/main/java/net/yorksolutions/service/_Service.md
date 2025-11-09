# Service

[Spring Data JPA Tutorial](https://www.youtube.com/watch?v=dKK2dZVLFug&list=PLGRDMO4rOGcPtMMl1DD2cu3GBPZloHOS_)

Spring applications, particularly with Spring Boot and Spring Data JPA, use the *Entity* (a.k.a., *Model*), *Repository* and *Service* architecture.

A *Service* is a middle-layer that interacts with a *Repository*. It encapsulates the business logic and helps perform more complex logic than simple CRUD operations.

## Steps to Create and Use JPA Repository
### Step 03 (continued from Repository)
The following is an example of "[injecting] the repository interface to another component and [using] the implementation that is provided automatically by Spring Data JPA”:

```java 
@Service
public class ProductService {
  // Our repository
  @Autowired
  private ProductRepository repository;

  public Product saveProduct(Product product) {
    // Calling our repository save() method
    return repository.save(product);
  }

  public List<Product> saveProducts() {
    // Calling our repository saveAll() method
    return repository.saveAll(product)
  }

  
  public List<Product> getProducts() {
    // Calling our repository findAll() method
    return repository.findAll();
  }

  public Product getProductById(int id) {
    return repository.findById(id).product;
  }
}
```

**Note:**
- The `@Autowired` annotation is being phased out by Spring
  - In lieu of using the annotation, create a `final` instance of it then add it into the constructor method
  - See snippet below (I am leaving the example above as-is to have an idea of what the `@Autowired` approach looks like)

```java
// DESC: Inject the following dependencies for Spring Context
// ... to auto-wire upon initialization
private final UserDetailsService userDetailsService;
public SecurityConfig(UserDetailsService userDetailsService) {
  this.userDetailsService = userDetailsService;
}
```