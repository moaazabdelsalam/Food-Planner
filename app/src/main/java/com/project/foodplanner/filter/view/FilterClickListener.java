package com.project.foodplanner.filter.view;

import com.project.foodplanner.model.Category;
import com.project.foodplanner.model.Country;
import com.project.foodplanner.model.Ingredient;
import com.project.foodplanner.model.Meal;

public interface FilterClickListener {
    void categoryClicked(Category category);

    void countryClicked(Country country);

    void ingredientClicked(Ingredient ingredient);

    void mealClicked(Meal meal);
}
