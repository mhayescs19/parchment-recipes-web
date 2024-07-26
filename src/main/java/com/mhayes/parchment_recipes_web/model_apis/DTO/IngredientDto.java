package com.mhayes.parchment_recipes_web.model_apis.DTO;

import com.mhayes.parchment_recipes_web.entities.recipe.Ingredient;
import com.mhayes.parchment_recipes_web.entities.recipe.Recipe;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class IngredientDto {
    private Optional<Long> id;

    private Double amount; // may use optional for amount-ingredientType

    private String unit;

    private String ingredientType;

    private Long recipeId; // foreign key back to recipe is critical for api
}