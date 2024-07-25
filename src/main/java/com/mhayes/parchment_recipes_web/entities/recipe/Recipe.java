package com.mhayes.parchment_recipes_web.entities.recipe;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
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

    private String author;

    private String description;

    private Time prepTime;

    private Time cookTime;

    // totalTime is calculated from prep and cook time

    private String servingYield;

    private String footNotes; // notes included from the original recipe

    private String userNotes; // a user's personal notes

    /*
    mapped by foreign key to one recipe; recipe is the entity class attribute name in ingredient
    cascadeType ensures that if a recipe is deleted, all remaining ingredients are also removed
    orphanRemoval ensures that there are no orphan ingredients without a valid FK to a recipe
    jsonManagedReference - serializes the object forwards, in this case looking for ingredients
     */
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Ingredient> ingredients = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Direction> directions = new ArrayList<>();


    public void addIngredient(Ingredient ingredient) {
        ingredient.setRecipe(this); // sets foreign key of ingredient to the appropriate recipe
        this.ingredients.add(ingredient);
    }
}
