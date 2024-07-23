package com.mhayes.parchment_recipes_web.model_apis;

import com.mhayes.parchment_recipes_web.entities.recipe.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    @Autowired // field injection of bean
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @GetMapping("/")
    public ResponseEntity<List<Recipe>> listRecipes() {
        Recipe newRecipe = new Recipe();
        newRecipe.setTitle("My Test Recipe");
        newRecipe.setSource_url("https://start.spring.io/");

        List<Ingredient> ingredientList = new ArrayList<>();

        ingredientList.add(Ingredient.builder() // prep ingredients list
                        .amount(5.0)
                        .unit("cups")
                        .ingredientType("flour")
                        .recipe(newRecipe) // set foreign key recipe back to recipe
                    .build());
        ingredientList.add(Ingredient.builder() // prep ingredients list
                .amount(2.0)
                .unit("tablespoons")
                .ingredientType("oil")
                .recipe(newRecipe)
                .build());

        List<Direction> directionList = new ArrayList<>();
        directionList.add(Direction.builder()
                        .content("Mix flour and oil until combined.")
                        .recipe(newRecipe)
                .build());
        directionList.add(Direction.builder()
                .content("Heat on medium for 5 minutes and serve immediately.")
                .recipe(newRecipe)
                .build());

        newRecipe.setIngredients(ingredientList);
        newRecipe.setDirections(directionList);

        recipeRepository.save(newRecipe);

        Recipe newRecipe2 = new Recipe();
        newRecipe2.setTitle("My Test Recipe 2");
        newRecipe2.setSource_url("https://www.jetbrains.com/help/idea/database-tool-window.html#overview");

        List<Ingredient> ingredientList2 = new ArrayList<>();

        ingredientList2.add(Ingredient.builder() // prep ingredients list
                .amount(0.3)
                .unit("cup")
                .ingredientType("sugar")
                .recipe(newRecipe2)
                .build());
        ingredientList2.add(Ingredient.builder() // prep ingredients list
                .amount(2.5)
                .unit("teaspoons")
                .ingredientType("vanilla")
                .recipe(newRecipe2)
                .build());
        ingredientList2.add(Ingredient.builder()
                        .amount(3.0)
                        .unit("tablespoons")
                        .ingredientType("water")
                        .recipe(newRecipe2)
                .build());

        List<Direction> directionList2 = new ArrayList<>();
        directionList2.add(Direction.builder()
                .content("Combine sugar, vanilla, and water in a medium bowl.")
                .recipe(newRecipe2)
                .build());
        directionList2.add(Direction.builder()
                .content("Place in the refrigerator for 24 hours.")
                .recipe(newRecipe2)
                .build());
        directionList2.add(Direction.builder()
                .content("Preheat the oven to 350° F.")
                .recipe(newRecipe2)
                .build());
        directionList2.add(Direction.builder()
                .content("Let mixture rest until room temperature is reached. Cook for 15 minutes until sugar is golden. Serve over oats.")
                .recipe(newRecipe2)
                .build());

        newRecipe2.setIngredients(ingredientList2);
        newRecipe2.setDirections(directionList2);

        recipeRepository.save(newRecipe2);

        return new ResponseEntity<>(recipeRepository.findAll(), HttpStatus.OK);
    }


}
