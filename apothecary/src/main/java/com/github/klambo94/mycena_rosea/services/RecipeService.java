package com.github.klambo94.mycena_rosea.services;

import com.github.klambo94.mycena_rosea.domain.dao.Recipe;
import com.github.klambo94.mycena_rosea.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public Set<Recipe> saveAllRecipes(Set<Recipe> recipes) {
        log.info("Saving all recipes {}", recipes);

        if(CollectionUtils.isEmpty(recipes)) {
            throw new IllegalArgumentException("Recipes are empty, unable to save object.");
        }
        return new HashSet<>(recipeRepository.saveAll(recipes));
    }

    public Recipe saveRecipe(Recipe recipe) {
        log.info("Saving recipe {}", recipe);

        if(recipe == null) {
            throw new IllegalArgumentException("Recipe is null, unable to save object.");
        }
        return recipeRepository.save(recipe);
    }

    public Recipe findRecipeById(Long id) {
        if(id == null) {
            throw new IllegalArgumentException("Recipe id is null, unable to retrieve object.");
        }
        return recipeRepository.findById(id).orElse(null);
    }

    public Recipe findRecipeByName(String name) {
        log.info("Fetching recipe with name: {}", name);
        return recipeRepository.findRecipeByName(name);
    }

    public List<Recipe> findAllRecipes() {
        log.info("Fetching all recipes and fully hydrating");
        return recipeRepository.findAllWithFullyHydrated();
    }

    public List<Recipe> findAllRecipesWithIngredients() {
        log.info("Fetching all recipes with ingredients");
        return recipeRepository.findAllWithIngredients();
    }

    public List<Recipe> findAllRecipesWithSteps() {
        log.info("Fetching all recipes with steps");
        return recipeRepository.findAllWithSteps();
    }

    public List<Recipe> findAllRecipesWithTags() {
        log.info("Fetching all recipes with tags");
        return recipeRepository.findAllWithTags();
    }

    public List<Recipe> findAllRecipesWithCharacteristics() {
        log.info("Fetching all recipes with characteristics");
        return recipeRepository.findAllWithCharacteristics();
    }

    public List<Recipe> findAllRecipesWithCharacteristicsAndProperties() {
        log.info("Fetching all recipes with characteristics and properties");
        return recipeRepository.findAllWithCharacteristicsAndProperties();
    }

    public Recipe updateRecipe(Recipe recipe) {
        log.info("Updating recipe {}", recipe);

        if(recipe == null || recipe.getName() == null) {
            throw new IllegalArgumentException("Recipe is null, unable to update object.");
        }

        // Check if the recipe exists by name (since name is unique)
        Recipe existingRecipe = recipeRepository.findRecipeByName(recipe.getName());

        if(existingRecipe != null) {
            log.info("Recipe found by name '{}', updating with id: {}", existingRecipe.getName(), existingRecipe.getId());
            // Copy new values to the existing entity, but preserve the id
            BeanUtils.copyProperties(recipe, existingRecipe, "id");
            return recipeRepository.save(existingRecipe);
        } else {
            log.info("Recipe not found, creating new one");
            // Clear the ID to ensure JPA treats it as a new entity
            recipe.setId(null);
            return recipeRepository.save(recipe);
        }
    }

    public void deleteRecipe(Long id) {
        log.info("Deleting recipe with id: {}", id);
        if(id == null) {
            throw new IllegalArgumentException("Recipe id is null, unable to delete object.");
        }
        recipeRepository.deleteById(id);
        log.info("Recipe deleted");
    }
}