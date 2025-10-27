package com.github.klambo94.mycena_rosea.repositories;

import com.github.klambo94.mycena_rosea.domain.dao.Item;
import com.github.klambo94.mycena_rosea.domain.dao.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepositoryImplementation<Product,Long> {

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.ingredients")
    List<Product> findAllWithIngredients();
}
