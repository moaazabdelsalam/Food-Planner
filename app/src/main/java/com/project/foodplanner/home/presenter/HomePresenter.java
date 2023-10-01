package com.project.foodplanner.home.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.project.foodplanner.home.view.HomeViewInterface;
import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.RepositoryInterface;
import com.project.foodplanner.model.RequestCode;
import com.project.foodplanner.network.NetworkCallback;

import java.util.List;

public class HomePresenter implements HomePresenterInterface, NetworkCallback {
    HomeViewInterface view;
    RepositoryInterface repository;

    public HomePresenter(HomeViewInterface view, RepositoryInterface repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void getRandomMeal() {
        repository.makeRandomMealCall(this);
    }

    @Override
    public void addMealToFavorite(Meal meal) {
        repository.addMealToDatabase(meal);
    }

    @Override
    public void onSuccessResult(RequestCode requestCode, JsonObject jsonObject) {
        switch (requestCode) {
            case RANDOM_MEAL_REQ:
                TypeToken<List<Meal>> mealTypeToken = new TypeToken<List<Meal>>() {
                };
                List<Meal> todayMeal = new Gson().fromJson(jsonObject.get("meals"), mealTypeToken.getType());
                view.showTodayMeal(todayMeal.get(0));
        }
    }

    @Override
    public void onFailureResult(String errorMsg) {

    }
}
