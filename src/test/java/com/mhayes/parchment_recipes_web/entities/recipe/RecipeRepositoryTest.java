package com.mhayes.parchment_recipes_web.entities.recipe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RecipeRepositoryTest {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Recipe myTestRecipe1;
    private Recipe myTestRecipe2;

    @Test
    @DisplayName("Verify ingredient foreign keys are set correctly")
    public void givenRecipeWithIngredients_whenSave_thenAllIngredientsHaveRecipeForeignKey() {
        /*
        Given: recipe with ingredients
         */
        Recipe myTestRecipe1 = new Recipe();
        myTestRecipe1.setTitle("My Test Recipe");
        myTestRecipe1.setSource_url("https://start.spring.io/");

        List<Ingredient> ingredientList = new ArrayList<>();

        ingredientList.add(Ingredient.builder() // prep ingredients list
                .amount(5.0)
                .unit("cups")
                .ingredientType("flour")
                .recipe(myTestRecipe1)
                .build());
        ingredientList.add(Ingredient.builder() // prep ingredients list
                .amount(2.0)
                .unit("tablespoons")
                .ingredientType("oil")
                .recipe(myTestRecipe1)
                .build());
        myTestRecipe1.setIngredients(ingredientList);

        Recipe myTestRecipe2 = new Recipe();
        myTestRecipe2.setTitle("My Test Recipe 2");
        myTestRecipe2.setSource_url("https://www.jetbrains.com/help/idea/database-tool-window.html#overview");

        List<Ingredient> ingredientList2 = new ArrayList<>();

        ingredientList2.add(Ingredient.builder() // prep ingredients list
                .amount(0.3)
                .unit("cup")
                .ingredientType("sugar")
                .recipe(myTestRecipe2)
                .build());
        ingredientList2.add(Ingredient.builder() // prep ingredients list
                .amount(2.5)
                .unit("teaspoons")
                .ingredientType("vanilla")
                .recipe(myTestRecipe2)
                .build());
        ingredientList2.add(Ingredient.builder()
                .amount(3.0)
                .unit("tablespoons")
                .ingredientType("water")
                .recipe(myTestRecipe2)
                .build());

        myTestRecipe2.setIngredients(ingredientList2);

        /*
        When: save recipe
         */
        Recipe myPersistedRecipe1 = recipeRepository.save(myTestRecipe1);
        Recipe myPersistedRecipe2 = recipeRepository.save(myTestRecipe2);
        /*
        Then: all ingredients have the correct recipe foreign key
         */
        myPersistedRecipe1.getIngredients().forEach(ingredient -> {
            assertEquals(myPersistedRecipe1.getId(),ingredient.getRecipe().getId()); // persisted recipe id matches the recipe id stored as a foreign key in the ingredient
        });

        myPersistedRecipe2.getIngredients().forEach(ingredient -> {
            assertEquals(myPersistedRecipe2.getId(),ingredient.getRecipe().getId());
        });

    }
}
