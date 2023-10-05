package com.project.foodplanner.home.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.project.foodplanner.model.PlanModel;
import com.project.foodplanner.model.SimpleMeal;
import com.project.foodplanner.network.FavoriteDelegate;
import com.project.foodplanner.home.view.HomeViewInterface;
import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.RepositoryInterface;
import com.project.foodplanner.model.RequestCode;
import com.project.foodplanner.network.NetworkCallback;
import com.project.foodplanner.utils.DummyCache;

import java.util.List;

public class HomePresenter implements HomePresenterInterface, NetworkCallback {
    private static final String TAG = "TAG home presenter";
    HomeViewInterface view;
    RepositoryInterface repository;

    public HomePresenter(HomeViewInterface view, RepositoryInterface repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void getTodayMeal() {
        if (DummyCache.getInstance().getTodayMealCache() != null) {
            view.showTodayMeal(DummyCache.getInstance().getTodayMealCache());
            return;
        }
        repository.makeRandomMealCall(this);
    }

    @Override
    public void addMealToFavorite() {

    }

    @Override
    public void todayMealFavoriteClick() {
        repository.todayMealFavoriteClick(new FavoriteDelegate() {
            @Override
            public void onSuccess(String mealName, int status) {
                view.showAddFavoriteMessage(mealName, status);
            }

            @Override
            public void onError(String error) {
                view.showAddFavoriteMessage("NAN", -1);
            }
        });
    }

    @Override
    public void sendMealID() {
        view.gotToMealDetails(repository.sendTodayMealId());
    }

    @Override
    public void addTodayMealToPlan(String dayID) {
        Log.i(TAG, "addTodayMealToPlan: sending request to repo to add today meal to plan");
        repository.todayMealAddToPlanClicked(dayID);
    }

    @Override
    public void onSuccessResult(RequestCode requestCode, JsonObject jsonObject) {
        switch (requestCode) {
            case RANDOM_MEAL_REQ:
                TypeToken<List<Meal>> mealTypeToken = new TypeToken<List<Meal>>() {
                };
                List<Meal> todayMeal = new Gson().fromJson(jsonObject.get("meals"), mealTypeToken.getType());
                DummyCache.getInstance().setTodayMealCache(todayMeal.get(0));
                view.showTodayMeal(todayMeal.get(0));
        }
    }

    @Override
    public void onFailureResult(RequestCode requestCode, String errorMsg) {

    }
}
