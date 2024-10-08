package com.mhayes.parchment_recipes_web.model;

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
public class Direction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 500)
    private String content;

    /*
    JsonBackReference - prevents recursion on the reference entity, in this case the details of the recipe in the relationship are ignored to prevent infinite recursion
    */
    @ManyToOne
    @JoinColumn(name = "recipe_id") // foreign key field
    @JsonBackReference
    private Recipe recipe;
}
