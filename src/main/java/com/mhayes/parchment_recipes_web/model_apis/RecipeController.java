package com.mhayes.parchment_recipes_web.model_apis;

import com.mhayes.parchment_recipes_web.entities.recipe.Recipe;
import com.mhayes.parchment_recipes_web.entities.recipe.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    @Autowired // field injection of bean
    private RecipeRepository repository;

    @GetMapping("/")
    public ResponseEntity<List<Recipe>> listRecipes() {
        repository.save(Recipe.builder()
                .title("My Test Recipe")
                .source_url("https://start.spring.io/")
                .build());

        repository.save(Recipe.builder()
                .title("Test recipe 2")
                .source_url("https://www.jetbrains.com/help/idea/database-tool-window.html#overview")
                .build());

        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }


}
