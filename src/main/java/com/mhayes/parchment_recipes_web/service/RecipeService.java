package com.mhayes.parchment_recipes_web.service;

import com.mhayes.parchment_recipes_web.model.IngredientRepository;
import com.mhayes.parchment_recipes_web.model.RecipeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional // Transactional ensures that a segment of work is done in full before managing another task so data is not corrupted
public class RecipeService {
    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    IngredientRepository ingredientRepository;


}
