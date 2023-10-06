package com.project.foodplanner.filterresult.presenter;

import com.project.foodplanner.model.Meal;
import com.project.foodplanner.network.DatabaseDelegate;

public interface FilterResultPresenterInterface {
    void filterByIngredient(String ingredient);

    void filterByCategory(String category);

    void filterByCountry(String country);

    void addToFavorite(Meal meal, DatabaseDelegate favoriteDelegate);

    void removeFromFavorite(Meal meal, DatabaseDelegate favoriteDelegate);

    void clearCache();
}
