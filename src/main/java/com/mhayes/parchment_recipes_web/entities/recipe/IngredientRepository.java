package com.mhayes.parchment_recipes_web.entities.recipe;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
