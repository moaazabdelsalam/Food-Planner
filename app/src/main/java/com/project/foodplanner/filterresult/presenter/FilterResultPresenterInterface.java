package com.project.foodplanner.filterresult.presenter;

import com.project.foodplanner.model.MealResponse;

import io.reactivex.rxjava3.core.Single;

public interface FilterResultPresenterInterface {
    void filterByIngredient(String ingredient);

    void filterByCategory(String category);

    void filterByCountry(String country);

    void clearCache();
}
