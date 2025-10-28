package com.github.klambo94.mycena_rosea.services;

import com.github.klambo94.mycena_rosea.domain.dao.Product;
import com.github.klambo94.mycena_rosea.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Set<Product> saveAllProducts(Set<Product> products) {
        log.info("Saving all products {}", products);

        if(CollectionUtils.isEmpty(products)) {
            throw new IllegalArgumentException("Products are empty, unable to save object.");
        }
        return new HashSet<>(productRepository.saveAll(products));
    }

    public Product saveProduct(Product product) {
        log.info("Saving product {}", product);

        if(product == null) {
            throw new IllegalArgumentException("Product is null, unable to save object.");
        }
        return productRepository.save(product);
    }

    public Product findItemById(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("Product id is null, unable to retrieve object.");
        }
        return productRepository.findById(id).orElse(null);
    }

    public Product findItemByName(String name) {
        log.info("Fetching product with name: {}", name);
        return productRepository.findProductByName(name);
    }

    public List<Product> findAllProducts() {
        log.info("Fetching all products and fully hydrating");
        return productRepository.findAllWithFullyHydrated();
    }

    public List<Product> findAllProductsWithIngredients() {
        log.info("Fetching all products with ingredients");
        return productRepository.findAllWithIngredients();
    }

    public List<Product> findAllProductsWithCharacteristics() {
        log.info("Fetching all products with characteristics");
        return productRepository.findAllWithCharacteristics();
    }

    public Product updateItem(Product product) {
        log.info("Updating product {}", product);

        if(product == null || product.getName() == null) {
            throw new IllegalArgumentException("Product is null, unable to update object.");
        }

        // Check if the product exists by name (since name is unique)
        Product existingProduct = productRepository.findProductByName(product.getName());

        if(existingProduct != null) {
            log.info("Product found by name '{}', updating with id: {}", existingProduct.getName(), existingProduct.getId());

            // If recipe_id hasn't changed, keep the existing recipe reference
            if (product.getRecipeId() != null &&
                    !product.getRecipeId().equals(existingProduct.getRecipeId())) {
                // Recipe is changing - detach the old one first
                existingProduct.setRecipe(null);
                productRepository.flush();
            }

            // Copy new values to the existing entity, but preserve the id
            BeanUtils.copyProperties(product, existingProduct, "id");
            return productRepository.save(existingProduct);
        } else {
            log.info("Product not found, creating new one");
            // Clear the ID to ensure JPA treats it as a new entity
            product.setId(null);
            return productRepository.save(product);
        }
    }


    public void deleteItem(Long id) {
        log.info("Deleting product with id: {}", id);
        if(id == null) {
            throw new IllegalArgumentException("Item id is null, unable to delete object.");
        }
        productRepository.deleteById(id);
        log.info("Product deleted");
    }
}