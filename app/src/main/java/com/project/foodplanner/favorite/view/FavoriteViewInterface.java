package com.project.foodplanner.favorite.view;

import com.project.foodplanner.model.Meal;

import java.util.List;

public interface FavoriteViewInterface {
    void removeMealFromFav(Meal meal);

    void showRemoveMealMessage(String mealName);

    void showNotLoggedInMessage();

    void showAllFavoriteMeals(List<Meal> meals);

    void showFavoriteMeal(Meal meal);

    void hidePlaceholders();

    void showPlaceholders();
}
