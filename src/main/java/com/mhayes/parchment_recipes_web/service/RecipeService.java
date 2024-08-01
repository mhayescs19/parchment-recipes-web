package com.mhayes.parchment_recipes_web.service;

import com.mhayes.parchment_recipes_web.dto.IngredientDto;
import com.mhayes.parchment_recipes_web.dto.enums.Resource;
import com.mhayes.parchment_recipes_web.exception.ResourceNotFoundException;
import com.mhayes.parchment_recipes_web.model.Ingredient;
import com.mhayes.parchment_recipes_web.model.IngredientRepository;
import com.mhayes.parchment_recipes_web.model.Recipe;
import com.mhayes.parchment_recipes_web.model.RecipeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
     * Overwrite a full recipe with updates and save to existing recipe in database
     * @param recipe updated recipe that references an existing recipe
     * @return
     */
    public Recipe updateRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public void deleteRecipe(Long recipeId) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if (recipe.isPresent()) {
            recipeRepository.deleteById(recipeId);
        } else {
            throw new ResourceNotFoundException(Resource.Recipe, recipeId);
        }
    }

    /**
     *
     * @param recipeId
     * @param ingredient
     * @return persisted ingredient
     * @throws ResourceNotFoundException if the path id does not correspond to a persisted recipe
     */
    public IngredientDto addIngredient(Long recipeId, IngredientDto ingredient) {
        Optional<Recipe> persistedRecipe = recipeRepository.findById(recipeId);

        if (persistedRecipe.isPresent()) { // validate that recipe FK in JSON is a real foreign key
            Ingredient newIngredient = Ingredient.builder()
                    .amount(ingredient.getAmount())
                    .unit(ingredient.getUnit())
                    .ingredientType(ingredient.getIngredientType())
                    .recipe(persistedRecipe.get())
                    .build();

            ingredientRepository.save(newIngredient);

            return ingredient;
        }

        throw new ResourceNotFoundException(Resource.Recipe, recipeId);
    }

    /**
     * Accepts an ingredient with one or all attributes non-null. Each attribute is checked and non-null values of the ingredient argument are updated in the persisted entity.
     * @param ingredientId id to an existing persisted ingredient
     * @param ingredientUpdate ingredient that has a minimum of one attribute initialized. all other fields can be null
     * @return persisted ingredient updates
     * @throws ResourceNotFoundException if the path id does not correspond to a persisted ingredient
     */
    public IngredientDto updateIngredient(Long ingredientId, IngredientDto ingredientUpdate) {
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

            return ingredientUpdate;
        }

        throw new ResourceNotFoundException(Resource.Ingredient, ingredientId);
    }

    public void deleteIngredient(Long ingredientId) {
        Optional<Ingredient> ingredient = ingredientRepository.findById(ingredientId);

        if (ingredient.isPresent()) {
            ingredientRepository.deleteById(ingredientId);
        } else {
            throw new ResourceNotFoundException(Resource.Ingredient, ingredientId);
        }
    }

    public List<Ingredient> listAllIngredients() {
        return ingredientRepository.findAll();
    }
}
