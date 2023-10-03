package com.project.foodplanner.filter.view;

import com.project.foodplanner.model.Category;
import com.project.foodplanner.model.Country;
import com.project.foodplanner.model.Ingredient;
import com.project.foodplanner.model.Meal;

import java.util.List;

public interface FilterViewInterface {
    void showCategoryList(List<Category> categoryList);

    void showIngredientList(List<Ingredient> ingredientList);

    void showCountryList(List<Country> countryList);

    void showMealList(List<Meal> mealList);
}
