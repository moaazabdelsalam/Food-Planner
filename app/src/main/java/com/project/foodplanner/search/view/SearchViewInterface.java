package com.project.foodplanner.search.view;

import com.project.foodplanner.model.Category;
import com.project.foodplanner.model.Ingredient;

import java.util.List;

public interface SearchViewInterface {
    void showCategoryList(List<Category> categoryList);

    void showIngredientList(List<Ingredient> ingredientList);
}