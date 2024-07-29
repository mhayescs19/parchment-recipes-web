package com.mhayes.parchment_recipes_web.controller;

import com.mhayes.parchment_recipes_web.dto.IngredientDto;
import com.mhayes.parchment_recipes_web.dto.RecipeDto;
import com.mhayes.parchment_recipes_web.model.*;
import com.mhayes.parchment_recipes_web.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    @Autowired // field injection of bean
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/")
    public ResponseEntity<List<Recipe>> listRecipes() {
        return new ResponseEntity<>(recipeService.listAllRecipes(), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) { // payload is a recipe json object
        //recipe.getIngredients().forEach(ingredient -> ingredient.setRecipe(recipe)); // set fk in ingredient
        //recipe.getDirections().forEach(ingredient -> ingredient.setRecipe(recipe));
        return new ResponseEntity<>(recipeService.createRecipe(recipe), HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<Recipe> updateRecipe(@RequestBody Recipe recipe) { // update entire recipe

        return new ResponseEntity<>(recipeService.updateRecipe(recipe), HttpStatus.OK);
    }

    // alt idea  = send a code in the packet of information to signal what attributes of the ingredient is being updated. use the code to immediately update the information

    /**
     * Update an existing ingredient's amount, unit, or ingredient type
     * @param ingredientToUpdate update ingredient in JSON
     * @return updated ingredient persisted in the recipe or not found status
     */
    @PatchMapping("/{ingredientId}/ingredient")
    public ResponseEntity<IngredientDto> updateIngredient(@PathVariable Long ingredientId, @RequestBody IngredientDto ingredientToUpdate) {
        return recipeService.updateIngredient(ingredientId, ingredientToUpdate);
    }

    /**
     * add new ingredient(s) to a recipe
     * @param recipeId
     * @param ingredient
     * @return
     */
    @PostMapping("/{recipeId}/ingredient")
    public ResponseEntity<IngredientDto> addIngredient(@PathVariable Long recipeId, @RequestBody IngredientDto ingredient) {
        return recipeService.addIngredient(recipeId, ingredient);
    }

    @PostMapping("/{recipeId}/ingredient/list")
    public ResponseEntity<List<Ingredient>> addIngredients(@PathVariable Long recipeId, @RequestBody List<IngredientDto> ingredientDtos) {
        List<Ingredient> persistedIngredients = new ArrayList<>();

        for (IngredientDto ingredientDto : ingredientDtos) {
            Optional<Recipe> persistedRecipe = recipeRepository.findById(recipeId);

            if (persistedRecipe.isPresent()) { // validate that recipe FK in JSON is a real foreign key
                Ingredient newIngredient = Ingredient.builder()
                        .amount(ingredientDto.getAmount())
                        .unit(ingredientDto.getUnit())
                        .ingredientType(ingredientDto.getIngredientType())
                        .recipe(persistedRecipe.get())
                        .build();

                ingredientRepository.save(newIngredient);
                persistedIngredients.add(newIngredient);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<>(persistedIngredients,HttpStatus.OK);
    }

    @GetMapping("/ingredients")
    public ResponseEntity<List<Ingredient>> listIngredients() {
        return new ResponseEntity<>(ingredientRepository.findAll(), HttpStatus.OK);
    }

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
        Optional<Ingredient> ingredient = ingredientRepository.findById(id);
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
    }


}

// api path names should be nouns for CRUD, not verbs!
// 404 - formatted payload, but does not exist
// 400 - request is malformed