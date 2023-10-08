package com.project.foodplanner.filterresult.view;

import com.project.foodplanner.model.Meal;

import java.util.List;

public interface FilterResultViewInterface {
    void updateAdapterList(List<Meal> mealList);

    void showFavoriteClickMessage(String mealName, int Status);

    void showAddToPlanMessage(String meal, int status);

    void showNotLoggedInMessage();
}
