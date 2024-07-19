package com.mhayes.parchment_recipes_web.model_apis;

import com.mhayes.parchment_recipes_web.entities.recipe.Ingredient;
import com.mhayes.parchment_recipes_web.entities.recipe.IngredientRepository;
import com.mhayes.parchment_recipes_web.entities.recipe.Recipe;
import com.mhayes.parchment_recipes_web.entities.recipe.RecipeRepository;
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
                        .recipe(newRecipe)
                    .build());
        ingredientList.add(Ingredient.builder() // prep ingredients list
                .amount(2.0)
                .unit("tablespoons")
                .ingredientType("oil")
                .recipe(newRecipe)
                .build());
        //ingredientRepository.save(ingredientList);
        newRecipe.setIngredients(ingredientList);

        recipeRepository.save(newRecipe);

        Recipe newRecipe2 = new Recipe();
        newRecipe2.setTitle("My Test Recipe 2");
        newRecipe2.setSource_url("https://www.jetbrains.com/help/idea/database-tool-window.html#overview");

        List<Ingredient> ingredientList2 = new ArrayList<>();

        ingredientList2.add(Ingredient.builder() // prep ingredients list
                .amount(0.3)
                .unit("cup")
                .ingredientType("sugar")
                .build());
        ingredientList2.add(Ingredient.builder() // prep ingredients list
                .amount(2.5)
                .unit("teaspoons")
                .ingredientType("vanilla")
                .build());
        ingredientList2.add(Ingredient.builder()
                        .amount(3.0)
                        .unit("water")
                        .ingredientType("water")

                .build());
        //ingredientRepository.save(ingredientList);
        newRecipe2.setIngredients(ingredientList2);

        recipeRepository.save(newRecipe2);

        /*newRecipe.addIngredient(Ingredient.builder() // prep ingredients list
                .amount(5.0)
                .unit("cups")
                .ingredientType("flour")
                .recipe(newRecipe)
                .build());
        newRecipe.addIngredient(Ingredient.builder() // prep ingredients list
                .amount(2.0)
                .unit("tablespoons")
                .ingredientType("oil")
                .recipe(newRecipe)
                .build());

        Recipe newRecipe2 = Recipe.builder()
                .title("Test recipe 2")
                .source_url("https://www.jetbrains.com/help/idea/database-tool-window.html#overview")
                .build();
        newRecipe2.addIngredient(Ingredient.builder() // prep ingredients list
                .amount(0.3)
                .unit("cup")
                .ingredientType("sugar")
                .build());
        newRecipe2.addIngredient(Ingredient.builder() // prep ingredients list
                .amount(2.5)
                .unit("teaspoons")
                .ingredientType("vanilla")
                .build());

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

        //newRecipe2.addIngredient(ingredientList2);

        recipeRepository.save(newRecipe2);*/

        return new ResponseEntity<>(recipeRepository.findAll(), HttpStatus.OK);
    }


}
