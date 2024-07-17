package com.mhayes.parchment_recipes_web.entities.recipe;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // unique identifier

    private String title;

    private String source_url;
    // title, source_url, author, description, prep_time, cook_time, total_time, yield, ingredients, instructions, foot_notes, user_notes
}
