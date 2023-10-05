package com.project.foodplanner.home.presenter;

public interface HomePresenterInterface {
    void getTodayMeal();

    void addMealToFavorite();

    void todayMealFavoriteClick();

    void sendMealID();

    void addTodayMealToPlan(String dayID);
}
