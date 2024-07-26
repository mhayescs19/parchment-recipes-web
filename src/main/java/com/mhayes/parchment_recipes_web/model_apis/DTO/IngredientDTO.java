package com.mhayes.parchment_recipes_web.model_apis.DTO;

import com.mhayes.parchment_recipes_web.entities.recipe.Ingredient;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class IngredientDTO {
    private Long id;
    private Optional<Double> amount;

    private Optional<String> unit;

    private Optional<String> ingredientType;
}
