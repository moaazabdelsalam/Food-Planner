package com.project.foodplanner.filter.presenter;

import com.project.foodplanner.model.MealResponse;
import com.project.foodplanner.model.ResponseModel;

import io.reactivex.rxjava3.core.Single;

public interface FilterPresenterInterface {
    void getAllCategories();

    void getAllIngredients();

    void getAllCountries();

    void filterMeals(String query);
    void filterCategories(String query);
    void filterCountries(String query);
    void filterIngredients(String query);

}
