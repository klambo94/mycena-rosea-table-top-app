package com.github.klambo94.mycena_rosea.repositories;

import com.github.klambo94.mycena_rosea.domain.dao.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.ingredients")
    List<Product> findAllWithIngredients();

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.tags")
    List<Product> findAllWithTags();

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.characteristics")
    List<Product> findAllWithCharacteristics();

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.characteristics c "
            + "LEFT JOIN FETCH c.charProperties")
    List<Product> findAllWithCharacteristicsAndProperties();

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.ingredients "
            + "LEFT JOIN FETCH p.characteristics c "
            + "LEFT JOIN FETCH c.charProperties "
            + "LEFT JOIN FETCH p.tags")
    List<Product> findAllWithFullyHydrated();

    Product findProductByName(String name);
}