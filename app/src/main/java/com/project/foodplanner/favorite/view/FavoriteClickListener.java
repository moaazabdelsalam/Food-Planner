package com.project.foodplanner.favorite.view;

import com.project.foodplanner.model.Category;
import com.project.foodplanner.model.Meal;

public interface FavoriteClickListener {
    void onFavIconClicked(Meal meal);

    void onCategoryTxtClicked(String category);

    void onCountryTxtClicked(String country);

    void onImgClicked(String mealId);
}
