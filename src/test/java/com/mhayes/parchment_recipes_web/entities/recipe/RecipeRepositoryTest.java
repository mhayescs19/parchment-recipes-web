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

/*
DataJpaTest - disables all autoconfiguration except those essential for JPA operations
ActiveProfiles - test tells spring to use application-test.properties file
AutoConfigureTestDatabase.Replace.NONE - spring not replace the current application datasource. since the ActiveProfiles annotation is set to "test, spring will use the DB that is configured in the properties(-test) file
    typically,  AutoConfigureTestDatabase will try to configure an H2 in-memory database which we do not want
 */
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RecipeRepositoryTest {
    @Autowired
    private RecipeRepository recipeRepository;

    private Recipe myTestRecipe1;
    private Recipe myTestRecipe2;

    @Test
    @DisplayName("Verify ingredient recipe_id foreign keys relate back to the recipe") // provides the test a custom name in the test suite
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

    @Test
    @DisplayName("Verify direction recipe_id foreign keys relate back to the recipe")
    public void givenRecipeWithDirections_whenSave_thenAllDirectionsHaveRecipeForeignKey() {
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

        List<Direction> directionList = new ArrayList<>();
        directionList.add(Direction.builder()
                .content("Mix flour and oil until combined.")
                .recipe(myTestRecipe1)
                .build());
        directionList.add(Direction.builder()
                .content("Heat on medium for 5 minutes and serve immediately.")
                .recipe(myTestRecipe1)
                .build());
        myTestRecipe1.setDirections(directionList);

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

        List<Direction> directionList2 = new ArrayList<>();
        directionList2.add(Direction.builder()
                .content("Combine sugar, vanilla, and water in a medium bowl.")
                .recipe(myTestRecipe2)
                .build());
        directionList2.add(Direction.builder()
                .content("Place in the refrigerator for 24 hours.")
                .recipe(myTestRecipe2)
                .build());
        directionList2.add(Direction.builder()
                .content("Preheat the oven to 350° F.")
                .recipe(myTestRecipe2)
                .build());
        directionList2.add(Direction.builder()
                .content("Let mixture rest until room temperature is reached. Cook for 15 minutes until sugar is golden. Serve over oats.")
                .recipe(myTestRecipe2)
                .build());

        myTestRecipe2.setDirections(directionList2);

        /*
        When: save recipe
         */
        Recipe myPersistedRecipe1 = recipeRepository.save(myTestRecipe1);
        Recipe myPersistedRecipe2 = recipeRepository.save(myTestRecipe2);
        /*
        Then: all ingredients have the correct recipe foreign key
         */
        myPersistedRecipe1.getDirections().forEach(direction -> {
            assertEquals(myPersistedRecipe1.getId(),direction.getRecipe().getId()); // persisted recipe id matches the recipe id stored as a foreign key in the ingredient
        });

        myPersistedRecipe2.getDirections().forEach(direction -> {
            assertEquals(myPersistedRecipe2.getId(),direction.getRecipe().getId());
        });

    }
}
