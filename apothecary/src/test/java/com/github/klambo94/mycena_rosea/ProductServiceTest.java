package com.github.klambo94.mycena_rosea;

import com.github.klambo94.mycena_rosea.domain.Type;
import com.github.klambo94.mycena_rosea.domain.dao.*;
import com.github.klambo94.mycena_rosea.services.ProductService;
import com.github.klambo94.mycena_rosea.services.RecipeService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private RecipeService recipeService;

    private Product product;
    private Recipe recipe;
    private Recipe recipe2;
    private Recipe recipe3;

    @BeforeEach
    void setUp() {
        // First, create and save a Recipe
        recipe = new Recipe();
        recipe.setName("Test Recipe for Product");
        recipe.setDescription("A recipe used for testing products");
        recipe.setPrepTime(10.0);
        recipe.setCookTime(20.0);
        recipe.setServings(4.0);
        recipe.setRecipeType(Type.POTION);

        // Save the recipe to the database
        recipe = recipeService.saveRecipe(recipe);
        log.info("Created recipe with id: {}", recipe.getId());


        recipe2 = new Recipe();
        recipe2.setName("Test Recipe for Product");
        recipe2.setDescription("A recipe used for testing products");
        recipe2.setPrepTime(10.0);
        recipe2.setCookTime(20.0);
        recipe2.setServings(4.0);
        recipe2.setRecipeType(Type.POTION);

        // Save the recipe to the database
        recipe2 = recipeService.saveRecipe(recipe2);
        log.info("Created recipe2 with id: {}", recipe.getId());

        recipe3 = new Recipe();
        recipe3.setName("Test Recipe for Product");
        recipe3.setDescription("A recipe used for testing products");
        recipe3.setPrepTime(10.0);
        recipe3.setCookTime(20.0);
        recipe3.setServings(4.0);
        recipe3.setRecipeType(Type.POTION);

        // Save the recipe to the database
        recipe3 = recipeService.saveRecipe(recipe3);
        log.info("Created recipe3 with id: {}", recipe.getId());
        // Now create the product with the persisted recipe
        product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Product Description");
        product.setItemType(Type.POTION);
        product.setPrice(25.0);
        product.setQuantity(5);

        // Use the actual persisted recipe instead of just an ID
        product.setRecipe(recipe);

        // Create ingredients
        Item ingredient1 = new Item();
        ingredient1.setName("Ingredient 1");
        ingredient1.setPrice(5.0);

        Item ingredient2 = new Item();
        ingredient2.setName("Ingredient 2");
        ingredient2.setPrice(10.0);

        product.setIngredients(new HashSet<>(Set.of(ingredient1, ingredient2)));

        // Create characteristics with properties
        CharProp prop1 = new CharProp("Product Prop 1", "Product Value 1");
        CharProp prop2 = new CharProp("Product Prop 2", "Product Value 2");

        Characteristic characteristic = new Characteristic("Product Characteristic",
                new HashSet<>(Set.of(prop1, prop2)));
        product.setCharacteristics(new HashSet<>(Set.of(characteristic)));

        // Create tags
        Tag tag1 = new Tag("Product Tag 1");
        Tag tag2 = new Tag("Product Tag 2");
        product.setTags(new HashSet<>(Set.of(tag1, tag2)));
    }

    @Test
    void contextLoads() {
        // Verify that ProductService was autowired successfully
        assert productService != null;
    }

    @Test
    void testSaveProduct() {
        product = productService.saveProduct(product);

        log.info("Saved product with id: {}", product.getId());
        assert product.getId() != null;
        assert product.getRecipe() != null;
        assert product.getRecipe().getId().equals(recipe.getId());
    }

    @Test
    void testSaveAllProducts() {
        Item ing1 = new Item();
        ing1.setName("Common Ingredient A");

        Item ing2 = new Item();
        ing2.setName("Common Ingredient B");

        Item ing3 = new Item();
        ing3.setName("Common Ingredient C");

        Product prod1 = new Product();
        prod1.setName("Product 1");
        prod1.setPrice(10.0);
        prod1.setIngredients(new HashSet<>(Set.of(ing1)));
        prod1.setRecipe(recipe);

        Product prod2 = new Product();
        prod2.setName("Product 2");
        prod2.setPrice(20.0);
        prod2.setIngredients(new HashSet<>(Set.of(ing2)));
        prod2.setRecipe(recipe2);

        Product prod3 = new Product();
        prod3.setName("Product 3");
        prod3.setPrice(30.0);
        prod3.setIngredients(new HashSet<>(Set.of(ing3)));
        prod3.setRecipe(recipe3);

        Set<Product> products = new HashSet<>(Set.of(prod1, prod2, prod3));

        Set<Product> savedProducts = productService.saveAllProducts(products);

        assert savedProducts.size() == 3;
        savedProducts.forEach(prod -> {
            log.info("Saved product: {} with id: {}", prod.getName(), prod.getId());
            assert prod.getId() != null;
        });
    }

    @Test
    void testSaveProductWithNullThrowsException() {
        try {
            productService.saveProduct(null);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Product is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testSaveAllProductsWithEmptySetThrowsException() {
        try {
            productService.saveAllProducts(new HashSet<>());
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Products are empty");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testFindProductById() {
        product = productService.saveProduct(product);

        Product fetchedProduct = productService.findItemById(product.getId());

        assert fetchedProduct != null;
        assert fetchedProduct.getId().equals(product.getId());
        assert fetchedProduct.getName().equals(product.getName());
        assert fetchedProduct.getRecipeId().equals(recipe.getId());

        log.info("Fetched product: {}", fetchedProduct.getName());
    }

    @Test
    void testFindProductByIdWithNullThrowsException() {
        try {
            productService.findItemById(null);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Product id is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testFindProductByName() {
        product = productService.saveProduct(product);

        Product fetchedProduct = productService.findItemByName(product.getName());

        assert fetchedProduct != null;
        assert fetchedProduct.getId().equals(product.getId());
        assert fetchedProduct.getName().equals(product.getName());

        log.info("Fetched product by name: {}", fetchedProduct.getName());
    }

    @Test
    void testFindAllProducts() {
        log.info("Testing fetch all products");
        product = productService.saveProduct(product);

        Product prod2 = new Product();
        prod2.setName("Test Product 2");
        prod2.setPrice(15.0);
        prod2.setRecipe(recipe2);
        prod2 = productService.saveProduct(prod2);
        log.info("Saved product with id: {}", prod2.getId());

        Product prod3 = new Product();
        prod3.setName("Test Product 3");
        prod3.setPrice(20.0);
        prod3.setRecipe(recipe3);
        prod3 = productService.saveProduct(prod3);
        log.info("Saved product with id: {}", prod3.getId());

        List<Product> products = productService.findAllProducts();

        assert !products.isEmpty();
        products.forEach(p -> log.info("Product: {}", p.getName()));
    }

    @Test
    void testFindAllProductsWithIngredients() {
        log.info("Testing fetch all products with ingredients");
        product = productService.saveProduct(product);

        List<Product> products = productService.findAllProductsWithIngredients();

        assert !products.isEmpty();
        products.forEach(p -> {
            log.info("Product: {} with {} ingredients", p.getName(),
                    p.getIngredients() != null ? p.getIngredients().size() : 0);
        });
    }

    @Test
    void testFindAllProductsWithCharacteristics() {
        log.info("Testing fetch all products with characteristics");
        product = productService.saveProduct(product);

        List<Product> products = productService.findAllProductsWithCharacteristics();

        assert !products.isEmpty();
        products.forEach(p -> {
            log.info("Product: {} with {} characteristics", p.getName(),
                    p.getCharacteristics() != null ? p.getCharacteristics().size() : 0);
        });
    }

    @Test
    void testUpdateProduct() {
        product = productService.saveProduct(product);

        product.setPrice(50.0);
        product.setDescription("Updated Product Description");

        product = productService.updateItem(product);

        assert product.getPrice() == 50.0;
        assert product.getDescription().equals("Updated Product Description");

        log.info("Updated product with id: {}", product.getId());
        log.info("Updated product with price: {}", product.getPrice());
    }

    @Test
    void testUpdateProductCreatesNewIfNotFound() {
        // Create a new recipe for this product
        Recipe newRecipe = new Recipe();
        newRecipe.setName("New Recipe");
        newRecipe.setDescription("New Recipe Description");
        newRecipe = recipeService.saveRecipe(newRecipe);

        Product newProduct = new Product();
        newProduct.setName("New Product");
        newProduct.setPrice(100.0);
        newProduct.setRecipe(newRecipe);

        Product savedProduct = productService.updateItem(newProduct);

        assert savedProduct.getId() != null;
        assert savedProduct.getName().equals("New Product");
        assert savedProduct.getPrice() == 100.0;

        log.info("Created new product during update with id: {}", savedProduct.getId());
    }

    @Test
    void testUpdateProductWithNullThrowsException() {
        try {
            productService.updateItem(null);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Product is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testUpdateProductWithNullNameThrowsException() {
        Product productWithoutName = new Product();
        productWithoutName.setPrice(10.0);

        try {
            productService.updateItem(productWithoutName);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Product is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testDeleteProduct() {
        product = productService.saveProduct(product);
        Long id = product.getId();

        productService.deleteItem(id);

        Product fetchedProduct = productService.findItemById(id);
        assert fetchedProduct == null;

        log.info("Successfully deleted product with id: {}", id);
    }

    @Test
    void testDeleteProductWithNullIdThrowsException() {
        try {
            productService.deleteItem(null);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Item id is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }
}