package com.mhayes.parchment_recipes_web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class IngredientDto extends ErrorDto {
    private Long id;

    private Double amount; // may use optional for amount-ingredientType

    private String unit;

    private String ingredientType;

    //private Long recipeId; // foreign key back to recipe is critical for api
}
