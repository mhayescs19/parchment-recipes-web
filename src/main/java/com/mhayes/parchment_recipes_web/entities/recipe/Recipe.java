package com.mhayes.parchment_recipes_web.entities.recipe;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // unique identifier

    private String title;

    private String source_url;
    // title, source_url, author, description, prep_time, cook_time, total_time, yield, ingredients, instructions, foot_notes, user_notes

    /*
    mapped by foreign key to one recipe
    cascadeType ensures that if a recipe is deleted, all remaining ingredients are also removed
    orphanRemoval ensures that there are no orphan ingredients without a valid FK to a recipe
    jsonManagedReference - serializes the object forwards, in this case looking for ingredients
     */
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Ingredient> ingredients = new ArrayList<>();


    public void addIngredient(Ingredient ingredient) {
        ingredient.setRecipe(this); // sets foreign key of ingredient to the appropriate recipe
        this.ingredients.add(ingredient);
    }
}
