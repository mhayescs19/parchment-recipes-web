package com.mhayes.parchment_recipes_web.service;

import com.mhayes.parchment_recipes_web.dto.IngredientDto;
import com.mhayes.parchment_recipes_web.dto.RecipeDto;
import com.mhayes.parchment_recipes_web.model.Ingredient;
import com.mhayes.parchment_recipes_web.model.IngredientRepository;
import com.mhayes.parchment_recipes_web.model.Recipe;
import com.mhayes.parchment_recipes_web.model.RecipeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional // Transactional ensures that a segment of work is done in full before managing another task so data is not corrupted
public class RecipeService {
    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    IngredientRepository ingredientRepository;

    public List<Recipe> listAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe createRecipe(Recipe recipe) {

        return recipeRepository.save(recipe);
    }

    /**
     * map updates to recipe in database
     * @param recipe
     * @return
     */
    public Recipe updateRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public ResponseEntity<IngredientDto> addIngredient(Long recipeId, IngredientDto ingredient) {
        Optional<Recipe> persistedRecipe = recipeRepository.findById(recipeId);

        if (persistedRecipe.isPresent()) { // validate that recipe FK in JSON is a real foreign key
            Ingredient newIngredient = Ingredient.builder()
                    .amount(ingredient.getAmount())
                    .unit(ingredient.getUnit())
                    .ingredientType(ingredient.getIngredientType())
                    .recipe(persistedRecipe.get())
                    .build();

            ingredientRepository.save(newIngredient);

            return new ResponseEntity<IngredientDto>(ingredient, HttpStatus.OK);
        }

        return new ResponseEntity<IngredientDto>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<IngredientDto> updateIngredient(Long ingredientId, IngredientDto ingredientUpdate) {
        Optional<Ingredient> persistedIngredient = ingredientRepository.findById(ingredientId); // search for ingredient

        /*
        private Double amount;

        private String unit;

        private String ingredientType;
         */
        if (persistedIngredient.isPresent()) { // validate that ingredient is found in database
            Ingredient validIngredient = persistedIngredient.get();
            /*
            Access the attributes of Ingredient
             */
            Double amount = ingredientUpdate.getAmount();
            String unit = ingredientUpdate.getUnit();
            String ingredientType = ingredientUpdate.getIngredientType();
            /*
            If the property in the payload is non-null, then update the value
             */
            if (ingredientUpdate.getAmount() != null) validIngredient.setAmount(ingredientUpdate.getAmount());
            if (ingredientUpdate.getUnit() != null) validIngredient.setUnit(ingredientUpdate.getUnit());
            if (ingredientUpdate.getIngredientType() !=  null) validIngredient.setIngredientType(ingredientUpdate.getIngredientType());

            Ingredient ingredient = ingredientRepository.save(validIngredient); // if id/ full ingredient should be sent back

            return new ResponseEntity<>(ingredientUpdate, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    //public ResponseEntity<Recipe>
}
