package com.project.foodplanner.filter.view;

import com.project.foodplanner.model.Category;
import com.project.foodplanner.model.Country;
import com.project.foodplanner.model.Ingredient;
import com.project.foodplanner.model.Meal;

public interface FilterClickListener {
    void categoryClicked(String category);

    void countryClicked(String country);

    void ingredientClicked(String ingredient);

    void mealClicked(String mealID);
}
