package com.github.klambo94.mycena_rosea;

import com.github.klambo94.mycena_rosea.domain.Type;
import com.github.klambo94.mycena_rosea.domain.dao.*;
import com.github.klambo94.mycena_rosea.services.RecipeService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@SpringBootTest
@Transactional
class RecipeServiceTest {

    @Autowired
    private RecipeService recipeService;

    static Recipe recipe;

    @BeforeAll
    static void setUp() {
        recipe = new Recipe();
        recipe.setName("Test Recipe");
        recipe.setDescription("Test Recipe Description");
        recipe.setRecipeType(Type.POTION);
        recipe.setPrepTime(15.0);
        recipe.setCookTime(30.0);
        recipe.setServings(4.0);

        // Create ingredients
        Item ingredient1 = new Item();
        ingredient1.setName("Recipe Ingredient 1");
        ingredient1.setPrice(5.0);

        Item ingredient2 = new Item();
        ingredient2.setName("Recipe Ingredient 2");
        ingredient2.setPrice(10.0);

        recipe.setIngredients(new HashSet<>(Set.of(ingredient1, ingredient2)));

        // Create steps
        Step step1 = new Step();
        step1.setStepNumber(1);
        step1.setInstruction("First step instruction");

        Step step2 = new Step();
        step2.setStepNumber(2);
        step2.setInstruction("Second step instruction");

        recipe.setSteps(new HashSet<>(Set.of(step1, step2)));

        // Create characteristics with properties
        CharProp prop1 = new CharProp("Recipe Prop 1", "Recipe Value 1");
        CharProp prop2 = new CharProp("Recipe Prop 2", "Recipe Value 2");

        Characteristic characteristic = new Characteristic("Recipe Characteristic",
                new HashSet<>(Set.of(prop1, prop2)));
        recipe.setCharacteristics(new HashSet<>(Set.of(characteristic)));

        // Create tags
        Tag tag1 = new Tag("Recipe Tag 1");
        Tag tag2 = new Tag("Recipe Tag 2");
        recipe.setTags(new HashSet<>(Set.of(tag1, tag2)));
    }

    @Test
    void contextLoads() {
        // Verify that RecipeService was autowired successfully
        assert recipeService != null;
    }

    @Test
    void testSaveRecipe() {
        recipe = recipeService.saveRecipe(recipe);

        log.info("Saved recipe with id: {}", recipe.getId());
        assert recipe.getId() != null;
    }

    @Test
    void testSaveAllRecipes() {
        Item ing1 = new Item();
        ing1.setName("Common Ingredient A");

        Item ing2 = new Item();
        ing2.setName("Common Ingredient B");

        Item ing3 = new Item();
        ing3.setName("Common Ingredient C");

        Recipe recipe1 = new Recipe();
        recipe1.setName("Recipe 1");
        recipe1.setDescription("First recipe");
        recipe1.setPrepTime(10.0);
        recipe1.setCookTime(20.0);
        recipe1.setServings(2.0);
        recipe1.setIngredients(new HashSet<>(Set.of(ing1)));

        Recipe recipe2 = new Recipe();
        recipe2.setName("Recipe 2");
        recipe2.setDescription("Second recipe");
        recipe2.setPrepTime(15.0);
        recipe2.setCookTime(25.0);
        recipe2.setServings(3.0);
        recipe2.setIngredients(new HashSet<>(Set.of(ing2)));

        Recipe recipe3 = new Recipe();
        recipe3.setName("Recipe 3");
        recipe3.setDescription("Third recipe");
        recipe3.setPrepTime(20.0);
        recipe3.setCookTime(30.0);
        recipe3.setServings(4.0);
        recipe3.setIngredients(new HashSet<>(Set.of(ing3)));

        Set<Recipe> recipes = new HashSet<>(Set.of(recipe1, recipe2, recipe3));

        Set<Recipe> savedRecipes = recipeService.saveAllRecipes(recipes);

        assert savedRecipes.size() == 3;
        savedRecipes.forEach(r -> {
            log.info("Saved recipe: {} with id: {}", r.getName(), r.getId());
            assert r.getId() != null;
        });
    }

    @Test
    void testSaveRecipeWithNullThrowsException() {
        try {
            recipeService.saveRecipe(null);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Recipe is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testSaveAllRecipesWithEmptySetThrowsException() {
        try {
            recipeService.saveAllRecipes(new HashSet<>());
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Recipes are empty");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testFindRecipeById() {
        recipe = recipeService.saveRecipe(recipe);

        Recipe fetchedRecipe = recipeService.findRecipeById(recipe.getId());

        assert fetchedRecipe != null;
        assert fetchedRecipe.getId().equals(recipe.getId());
        assert fetchedRecipe.getName().equals(recipe.getName());
        assert fetchedRecipe.getPrepTime() == recipe.getPrepTime();
        assert fetchedRecipe.getCookTime() == recipe.getCookTime();

        log.info("Fetched recipe: {}", fetchedRecipe.getName());
    }

    @Test
    void testFindRecipeByIdWithNullThrowsException() {
        try {
            recipeService.findRecipeById(null);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Recipe id is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testFindRecipeByName() {
        recipe = recipeService.saveRecipe(recipe);

        Recipe fetchedRecipe = recipeService.findRecipeByName(recipe.getName());

        assert fetchedRecipe != null;
        assert fetchedRecipe.getId().equals(recipe.getId());
        assert fetchedRecipe.getName().equals(recipe.getName());

        log.info("Fetched recipe by name: {}", fetchedRecipe.getName());
    }

    @Test
    void testFindAllRecipes() {
        log.info("Testing fetch all recipes");
        recipe = recipeService.saveRecipe(recipe);

        Recipe recipe2 = new Recipe();
        recipe2.setName("Test Recipe 2");
        recipe2.setDescription("Second test recipe");
        recipe2.setPrepTime(10.0);
        recipe2.setCookTime(15.0);
        recipe2.setServings(2.0);
        recipe2 = recipeService.saveRecipe(recipe2);
        log.info("Saved recipe with id: {}", recipe2.getId());

        Recipe recipe3 = new Recipe();
        recipe3.setName("Test Recipe 3");
        recipe3.setDescription("Third test recipe");
        recipe3.setPrepTime(20.0);
        recipe3.setCookTime(25.0);
        recipe3.setServings(6.0);
        recipe3 = recipeService.saveRecipe(recipe3);
        log.info("Saved recipe with id: {}", recipe3.getId());

        List<Recipe> recipes = recipeService.findAllRecipes();

        assert !recipes.isEmpty();
        recipes.forEach(r -> log.info("Recipe: {}", r.getName()));
    }

    @Test
    void testFindAllRecipesWithIngredients() {
        log.info("Testing fetch all recipes with ingredients");
        recipe = recipeService.saveRecipe(recipe);

        List<Recipe> recipes = recipeService.findAllRecipesWithIngredients();

        assert !recipes.isEmpty();
        recipes.forEach(r -> {
            log.info("Recipe: {} with {} ingredients", r.getName(),
                    r.getIngredients() != null ? r.getIngredients().size() : 0);
        });
    }

    @Test
    void testFindAllRecipesWithSteps() {
        log.info("Testing fetch all recipes with steps");
        recipe = recipeService.saveRecipe(recipe);

        List<Recipe> recipes = recipeService.findAllRecipesWithSteps();

        assert !recipes.isEmpty();
        recipes.forEach(r -> {
            log.info("Recipe: {} with {} steps", r.getName(),
                    r.getSteps() != null ? r.getSteps().size() : 0);
        });
    }

    @Test
    void testFindAllRecipesWithTags() {
        log.info("Testing fetch all recipes with tags");
        recipe = recipeService.saveRecipe(recipe);

        List<Recipe> recipes = recipeService.findAllRecipesWithTags();

        assert !recipes.isEmpty();
        recipes.forEach(r -> {
            log.info("Recipe: {} with {} tags", r.getName(),
                    r.getTags() != null ? r.getTags().size() : 0);
        });
    }

    @Test
    void testFindAllRecipesWithCharacteristics() {
        log.info("Testing fetch all recipes with characteristics");
        recipe = recipeService.saveRecipe(recipe);

        List<Recipe> recipes = recipeService.findAllRecipesWithCharacteristics();

        assert !recipes.isEmpty();
        recipes.forEach(r -> {
            log.info("Recipe: {} with {} characteristics", r.getName(),
                    r.getCharacteristics() != null ? r.getCharacteristics().size() : 0);
        });
    }

    @Test
    void testFindAllRecipesWithCharacteristicsAndProperties() {
        log.info("Testing fetch all recipes with characteristics and properties");
        recipe = recipeService.saveRecipe(recipe);

        List<Recipe> recipes = recipeService.findAllRecipesWithCharacteristicsAndProperties();

        assert !recipes.isEmpty();
        recipes.forEach(r -> {
            log.info("Recipe: {} with {} characteristics", r.getName(),
                    r.getCharacteristics() != null ? r.getCharacteristics().size() : 0);
            if (r.getCharacteristics() != null) {
                r.getCharacteristics().forEach(c -> {
                    log.info("  Characteristic: {} with {} properties", c.getName(),
                            c.getCharProperties() != null ? c.getCharProperties().size() : 0);
                });
            }
        });
    }

    @Test
    void testUpdateRecipe() {
        recipe = recipeService.saveRecipe(recipe);

        recipe.setPrepTime(25.0);
        recipe.setCookTime(40.0);
        recipe.setDescription("Updated Recipe Description");

        recipe = recipeService.updateRecipe(recipe);

        assert recipe.getPrepTime() == 25.0;
        assert recipe.getCookTime() == 40.0;
        assert recipe.getDescription().equals("Updated Recipe Description");

        log.info("Updated recipe with id: {}", recipe.getId());
        log.info("Updated recipe prep time: {}", recipe.getPrepTime());
        log.info("Updated recipe cook time: {}", recipe.getCookTime());
    }

    @Test
    void testUpdateRecipeCreatesNewIfNotFound() {
        Recipe newRecipe = new Recipe();
        newRecipe.setName("New Recipe");
        newRecipe.setDescription("Brand new recipe");
        newRecipe.setPrepTime(10.0);
        newRecipe.setCookTime(20.0);
        newRecipe.setServings(2.0);

        Recipe savedRecipe = recipeService.updateRecipe(newRecipe);

        assert savedRecipe.getId() != null;
        assert savedRecipe.getName().equals("New Recipe");
        assert savedRecipe.getPrepTime() == 10.0;

        log.info("Created new recipe during update with id: {}", savedRecipe.getId());
    }

    @Test
    void testUpdateRecipeWithNullThrowsException() {
        try {
            recipeService.updateRecipe(null);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Recipe is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testUpdateRecipeWithNullNameThrowsException() {
        Recipe recipeWithoutName = new Recipe();
        recipeWithoutName.setDescription("Recipe without name");
        recipeWithoutName.setPrepTime(10.0);

        try {
            recipeService.updateRecipe(recipeWithoutName);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Recipe is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }

    @Test
    void testDeleteRecipe() {
        recipe = recipeService.saveRecipe(recipe);
        Long id = recipe.getId();

        recipeService.deleteRecipe(id);

        Recipe fetchedRecipe = recipeService.findRecipeById(id);
        assert fetchedRecipe == null;

        log.info("Successfully deleted recipe with id: {}", id);
    }

    @Test
    void testDeleteRecipeWithNullIdThrowsException() {
        try {
            recipeService.deleteRecipe(null);
            assert false : "Expected IllegalArgumentException";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("Recipe id is null");
            log.info("Correctly threw exception: {}", e.getMessage());
        }
    }
}