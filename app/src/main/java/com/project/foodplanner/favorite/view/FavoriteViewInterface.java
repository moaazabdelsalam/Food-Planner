package com.project.foodplanner.favorite.view;

import com.project.foodplanner.model.Meal;

public interface FavoriteViewInterface {
    void removeMealFromFav(Meal meal);

    void showRemoveMealMessage(String mealName);

    void showNotLoggedInMessage();
}
