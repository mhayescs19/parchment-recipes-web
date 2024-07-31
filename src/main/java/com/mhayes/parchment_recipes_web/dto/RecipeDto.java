package com.mhayes.parchment_recipes_web.dto;

import java.sql.Time;
import java.util.List;

public class RecipeDto extends ErrorDto {
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

    private List<IngredientDto> ingredients;

    private List<DirectionDto> directions;
}
