package com.mhayes.parchment_recipes_web.entities.recipe;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Double amount;

    private String unit;

    private String ingredientType;

    @ManyToOne
    @JoinColumn(name = "recipe_id") // foreign key field
    @JsonBackReference
    private Recipe recipe;
}
