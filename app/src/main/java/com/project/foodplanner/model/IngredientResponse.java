package com.project.foodplanner.model;

import java.util.List;

public class IngredientResponse {
    List<Ingredient> ingredients;

    public IngredientResponse(List<Ingredient> resultList) {
        this.ingredients = resultList;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
