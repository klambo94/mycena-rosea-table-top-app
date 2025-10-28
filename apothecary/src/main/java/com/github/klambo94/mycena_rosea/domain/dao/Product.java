package com.github.klambo94.mycena_rosea.domain.dao;

import com.github.klambo94.mycena_rosea.domain.Type;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("1")
public class Product extends Item {

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "recipe_id", unique = true)
    private Recipe recipe;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Item> ingredients;

    public Product(String name, String description, Type itemType,
                   double price, double quantity, double weight,
                   String imagePath, String imageName,
                   Set<Characteristic> characteristics, Set<Tag> tags,
                   Recipe recipe, Set<Item> ingredients) {
        super(name, description, itemType, price, quantity, weight, imagePath, imageName, characteristics, tags);
        this.recipe = recipe;
        this.ingredients = ingredients;
    }

    // Convenience methods for working with recipe ID
    public Long getRecipeId() {
        return recipe != null ? recipe.getId() : null;
    }

    public void setRecipeId(Long recipeId) {
        if (recipeId != null) {
            Recipe r = new Recipe();
            r.setId(recipeId);
            this.recipe = r;
        } else {
            this.recipe = null;
        }
    }

}
