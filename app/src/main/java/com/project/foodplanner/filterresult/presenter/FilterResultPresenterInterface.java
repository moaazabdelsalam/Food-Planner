package com.project.foodplanner.filterresult.presenter;

import com.project.foodplanner.model.Meal;
import com.project.foodplanner.network.FavoriteDelegate;

public interface FilterResultPresenterInterface {
    void filterByIngredient(String ingredient);

    void filterByCategory(String category);

    void filterByCountry(String country);

    void addToFavorite(Meal meal, FavoriteDelegate favoriteDelegate);

    void removeFromFavorite(Meal meal, FavoriteDelegate favoriteDelegate);

    void clearCache();
}
