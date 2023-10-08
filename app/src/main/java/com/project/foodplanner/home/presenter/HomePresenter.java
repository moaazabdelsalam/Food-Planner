package com.project.foodplanner.home.presenter;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.project.foodplanner.database.PlanDelegate;
import com.project.foodplanner.model.CloudRepo;
import com.project.foodplanner.model.CloudRepoInterface;
import com.project.foodplanner.model.PlanModel;
import com.project.foodplanner.model.SimpleMeal;
import com.project.foodplanner.network.DatabaseDelegate;
import com.project.foodplanner.home.view.HomeViewInterface;
import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.MealsRepositoryInterface;
import com.project.foodplanner.model.RequestCode;
import com.project.foodplanner.network.NetworkCallback;
import com.project.foodplanner.network.NetworkDelegate;
import com.project.foodplanner.utils.DummyCache;

import java.util.List;

public class HomePresenter implements HomePresenterInterface, NetworkCallback, DatabaseDelegate, NetworkDelegate {
    private static final String TAG = "TAG home presenter";
    HomeViewInterface view;
    MealsRepositoryInterface repository;
    CloudRepoInterface cloudRepo;

    public HomePresenter(HomeViewInterface view, MealsRepositoryInterface repository, CloudRepo cloudRepo) {
        this.view = view;
        this.repository = repository;
        this.cloudRepo = cloudRepo;
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
    public void addMealToFavorite(Meal meal) {
        if (cloudRepo.getCurrentUser() == null) {
            view.showNotLoggedInMessage();
        } else {
            meal.setUserID(cloudRepo.getCurrentUser().getUid());
            repository.addMealToFavDB(meal, this);
            //cloudRepo.addFavMealToRemoteDB(meal);
        }
    }

    @Override
    public void removeFromFavorite(Meal meal) {
        if (cloudRepo.getCurrentUser() == null)
            view.showNotLoggedInMessage();
        else
            repository.removeMealFromFavDB(meal, this);
    }

    @Override
    public void todayMealFavoriteClick() {
        if (cloudRepo.getCurrentUser() == null)
            view.showNotLoggedInMessage();
        else {
            repository.todayMealFavoriteClick(this);
        }
    }

    @Override
    public void sendMealID() {
        view.goToMealDetails(repository.sendTodayMealId());
    }

    @Override
    public void addTodayMealToPlan(String dayID) {
        if (cloudRepo.getCurrentUser() == null)
            view.showNotLoggedInMessage();
        else {
            Log.i(TAG, "addTodayMealToPlan: sending request to repo to add today meal to plan");
            repository.todayMealAddToPlanClicked(dayID, new DatabaseDelegate() {
                @Override
                public void onSuccess(String mealName, int status) {
                    view.showAddToPlanMessage(mealName, status);
                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }

    @Override
    public void getTodayPlan(String dayID) {
        Log.i(TAG, "getTodayPlan: ");
        if (cloudRepo.getCurrentUser() != null)
            //view.showNotLoggedInMessage();

            repository.getAllPlansOfDay(dayID, new PlanDelegate() {
                @Override
                public void onSuccess(SimpleMeal planSimpleMeal, String dayID) {
                    view.showTodayPlanMeal(planSimpleMeal);
                }

                @Override
                public void onError(String error) {

                }
            });
        }

        @Override
        public void getMealsOfCountry (String country){
            repository.getRegionMeals(country, this);
        }

        @Override
        public void addMealToPlan (Meal meal, String dayId){
            if (cloudRepo.getCurrentUser() == null)
                view.showNotLoggedInMessage();
            else
                repository.insertPlan(
                        new PlanModel(dayId, meal.getIdMeal()),
                        new SimpleMeal(meal.getIdMeal(),
                                meal.getStrMeal(),
                                meal.getStrCategory(),
                                meal.getStrArea(),
                                meal.getStrMealThumb(),
                                meal.getStrTags()),
                        new DatabaseDelegate() {
                            @Override
                            public void onSuccess(String mealName, int status) {
                                view.showAddToPlanMessage(mealName, status);
                            }

                            @Override
                            public void onError(String error) {

                            }
                        }
                );
        }

        @Override
        public FirebaseUser getCurrentUser () {
            return cloudRepo.getCurrentUser();
        }

        @Override
        public void onSuccessResult (RequestCode requestCode, JsonObject jsonObject){
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
        public void onFailureResult (RequestCode requestCode, String errorMsg){

        }

        @Override
        public void onSuccess (String mealName,int status){
            view.showAddFavoriteMessage(mealName, status);
        }

        @Override
        public void onSuccess (List < Meal > mealList) {
            view.showCountryMeals(mealList);
        }

        @Override
        public void onError (String error){

        }
    }
