package com.project.foodplanner.search.view;

public interface SearchClickListener {
    void onCategoryClicked(String category);

    void onIngredientClicked(String ingredient);

    void onCategoryShowAllClicked();

    void onIngredientShowAllClicked();
}
