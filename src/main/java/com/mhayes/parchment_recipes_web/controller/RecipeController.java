package com.mhayes.parchment_recipes_web.controller;

import com.mhayes.parchment_recipes_web.dto.IngredientDto;
import com.mhayes.parchment_recipes_web.dto.RecipeDto;
import com.mhayes.parchment_recipes_web.model.*;
import com.mhayes.parchment_recipes_web.service.RecipeService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/")
    public ResponseEntity<List<Recipe>> listRecipes() {
        return new ResponseEntity<>(recipeService.listAllRecipes(), HttpStatus.OK);
    }

    @PostMapping(value = "/")
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) { // payload is a recipe json object
        return new ResponseEntity<>(recipeService.createRecipe(recipe), HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<Recipe> updateRecipe(@RequestBody Recipe recipe) { // update entire recipe
        return new ResponseEntity<>(recipeService.updateRecipe(recipe), HttpStatus.OK);
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<HttpStatus> deleteRecipe(@PathVariable Long recipeId) {
        recipeService.deleteRecipe(recipeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Long recipeId) {
        return new ResponseEntity<>(recipeService.getRecipe(recipeId), HttpStatus.OK);
    }

    /**
     * Update an existing ingredient's amount, unit, and/or ingredient type.
     * @param ingredientToUpdate deserialized JSON payload that contains <strong>only</strong> the edits requested for a persisted ingredient
     * @return updated ingredient persisted in the recipe or resource not found status
     */
    @PatchMapping("/{ingredientId}/ingredient")
    public ResponseEntity<IngredientDto> updateIngredient(@PathVariable Long ingredientId, @Valid @RequestBody IngredientDto ingredientToUpdate) {
        return new ResponseEntity<>(recipeService.updateIngredient(ingredientId, ingredientToUpdate), HttpStatus.OK);
    }

    /**
     * add a new ingredient to an existing recipe
     * @param recipeId id for recipe to host the new ingredient
     * @param ingredient deserialized JSON payload of ingredient contents
     * @return new ingredient persisted in the recipe or resource not found status
     */
    @PostMapping("/{recipeId}/ingredient")
    public ResponseEntity<IngredientDto> addIngredient(@PathVariable Long recipeId, @RequestBody IngredientDto ingredient) {
        return new ResponseEntity<>(recipeService.addIngredient(recipeId, ingredient), HttpStatus.OK);
    }

    @DeleteMapping("/{ingredientId}/ingredient")
    public ResponseEntity<HttpStatus> deleteIngredient(@PathVariable Long ingredientId) {
        recipeService.deleteIngredient(ingredientId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{recipeId}/ingredient/list")
    public ResponseEntity<?> addIngredients(@PathVariable Long recipeId, @Validated @RequestBody List<IngredientDto> ingredientDtos) {
        for (IngredientDto ingredientDto : ingredientDtos) {
            recipeService.addIngredient(recipeId, ingredientDto);
        }

        return new ResponseEntity<>(ingredientDtos,HttpStatus.OK);
    }

    @GetMapping("/ingredients")
    public ResponseEntity<List<Ingredient>> listIngredients() {
        return new ResponseEntity<>(recipeService.listAllIngredients(), HttpStatus.OK);
    }

    /*
    @PostMapping("/create")
    public ResponseEntity<List<Recipe>> createRecipes() {
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

        return new ResponseEntity<>(recipeRepository.findAll(), HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteIngredient/{id}")
    public ResponseEntity<Ingredient> deleteIngredientById(@PathVariable Long id) {
        /*
        Endpoint can receive an invalid id so verify that the id to delete exists
         */
        /*Optional<Ingredient> ingredient = ingredientRepository.findById(id);
        if (ingredient.isPresent()) {
            ingredientRepository.deleteById(id);
            return new ResponseEntity<>(ingredient.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/deleteRecipe/{id}")
    public ResponseEntity<Recipe> deleteRecipeById(@PathVariable Long id) {

        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (recipe.isPresent()) {
            recipeRepository.deleteById(id);
            return new ResponseEntity<>(recipe.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }*/


}

// api path names should be nouns for CRUD, not verbs!
// 404 - formatted payload, but does not exist
// 400 - request is malformed
