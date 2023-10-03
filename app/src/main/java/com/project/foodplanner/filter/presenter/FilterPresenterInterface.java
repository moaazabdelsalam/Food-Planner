package com.project.foodplanner.filter.presenter;

public interface FilterPresenterInterface {
    void getAllCategories();

    void getAllIngredients();

    void getAllCountries();

    void filterMeals(String query);
    void filterCategories(String query);
    void filterCountries(String query);
    void filterIngredients(String query);

}
