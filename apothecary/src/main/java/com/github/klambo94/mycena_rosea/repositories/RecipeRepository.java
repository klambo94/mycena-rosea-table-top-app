package com.github.klambo94.mycena_rosea.repositories;

import com.github.klambo94.mycena_rosea.domain.dao.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("SELECT r FROM Recipe r LEFT JOIN FETCH r.ingredients")
    List<Recipe> findAllWithIngredients();

    @Query("SELECT r FROM Recipe r LEFT JOIN FETCH r.steps")
    List<Recipe> findAllWithSteps();

    @Query("SELECT r FROM Recipe r LEFT JOIN FETCH r.tags")
    List<Recipe> findAllWithTags();

    @Query("SELECT r FROM Recipe r LEFT JOIN FETCH r.characteristics")
    List<Recipe> findAllWithCharacteristics();

    @Query("SELECT r FROM Recipe r LEFT JOIN FETCH r.characteristics c "
            + "LEFT JOIN FETCH c.charProperties")
    List<Recipe> findAllWithCharacteristicsAndProperties();

    @Query("SELECT r FROM Recipe r LEFT JOIN FETCH r.ingredients "
            + "LEFT JOIN FETCH r.steps "
            + "LEFT JOIN FETCH r.characteristics c "
            + "LEFT JOIN FETCH c.charProperties "
            + "LEFT JOIN FETCH r.tags")
    List<Recipe> findAllWithFullyHydrated();

    Recipe findRecipeByName(String name);
}