# Unit Testing

[Spring Data JPA Tutorial](https://www.youtube.com/watch?v=dKK2dZVLFug&list=PLGRDMO4rOGcPtMMl1DD2cu3GBPZloHOS_)

The tutorial discusses Unit Testing with JUnit, however I will not yet be completing this so the following is just for informative purposes.

## Easily Generate Test File
In the specific Repository file, right-click on the class-name, hover over Go To and click Test. In the prompt, click on Create New Test and select JUnit 5.

This will, automatically, create a file in the */src/test/…* directory.

## Example Test File
The following can be ran by clicking on the run-icon in the gutter of the test file.

```java
@SpringBootTest
class ProductRepositoryTest {

  @Autowired
  private ProductRepository productRepository;

  @Test
  void saveMethod() {
    // Create product
    Product product = new Product();
    product.setName("product");
    product.setDescription("product one description");
    product.setSku("10031");
    product.setPrice(new BigDecimal(100));
    product.setActive(true);
    product.setImageUrl("productOne.png");

    // Save product
    Product savedObject = productRepository.save(product);
    
    // Display product information
    System.out.println(savedObject.getId());
    System.out.println(savedObject.toString());
  }

  @Test
  void updateUsingSaveMethod() {
    // Find/Retrieve entity by ID
    // Note: Using a DB GUI, you can pull the ID from the DB for the
    // ... test data-entry that you want to update
    Long id = 1L;
    Product product = productRepository.findById(id).get();

    // Update entity information
    product.setName("updated product one");
    product.setDescription("updated product one desc.");

    // Save updated entity
    productRepository.save(product);
  }

  @Test
  void findByIdMethod() {
    // Note: Using a DB GUI, you can pull the ID from the DB for the
    // ... test data-entry that you want to update
    Long id = 1L;

    // Return a Product-Entity object
    Product product = productRepository.findById(id).get();
  }

  @Test
  void saveAllMethod() {
    // Create product
    Product product = new Product();
    product.setName("product 2");
    product.setDescription("product two description");
    product.setSku("10055");
    product.setPrice(new BigDecimal(120));
    product.setActive(true);
    product.setImageUrl("productTwo.png");

    // Create product
    Product product3 = new Product();
    product.setName("product 3");
    product.setDescription("product three description");
    product.setSku("10065");
    product.setPrice(new BigDecimal(125));
    product.setActive(true);
    product.setImageUrl("productThree.png");

    // Save products
    productRepository.saveAll(List.of(product, product3));
  }

  @Test
  void findAllMethod() {
    // Retrieve all products and assign to a List
    List<Product> products = productRepository.findAll();

    // Using a for-loop and a lambda expression, loop through
    // ... each 'product'
    products.forEach((p) -> {
      System.out.println(p.getName());
    });
  }

  // Deleteing by ID is more efficient than the Delete method
  // ... because there is no need for the retrieval step
  @Test
  void deleteByIdMethod() {
    Long id = 1L;
    productRepository.deleteById(id);
  }

  @Test
  void deleteMethod() {
    // Retrieve the element by the ID
    Long id = 2L;
    Product product = productRepository.findById(id).get();

    // Delete the entity
    productRepository.delete(product);
  }

  @Test
  void deleteAllMethod() {
    // Retrieve multiple products
    Product product1 = productRepository.findById(5L).get();
    Product product2 = productRepository.findById(6L).get();

    // Delete those products by converting individual products to List
    productRepository.deleteAll(List.of(product1, product2));
  }

  @Test
  void countMethod() {
    Long count = productRepository.count();
    System.out.println(count);
  }
}
```