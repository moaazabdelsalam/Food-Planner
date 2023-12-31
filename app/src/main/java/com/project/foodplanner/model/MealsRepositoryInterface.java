package com.project.foodplanner.model;

import androidx.lifecycle.LiveData;

import com.project.foodplanner.database.PlanDelegate;
import com.project.foodplanner.network.DatabaseDelegate;
import com.project.foodplanner.network.NetworkCallback;
import com.project.foodplanner.network.NetworkDelegate;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface MealsRepositoryInterface {
    void makeCategoryListCall(NetworkCallback networkCallback);

    void makeCountryListCall(NetworkCallback networkCallback);

    void makeIngredientListCall(NetworkCallback networkCallback);

    void makeRandomMealCall(NetworkCallback networkCallback);

    Single<MealResponse> searchByFirstCharacterCall(char charToSearchWith);

    Single<MealResponse> filterByIngredient(String ingredient);

    Single<MealResponse> filterByCategory(String category);

    void filterByCountry(String country, NetworkDelegate networkDelegate);

    void getRegionMeals(String country, NetworkDelegate networkDelegate);

    void getMealById(String mealId, NetworkDelegate networkDelegate);

    void todayMealFavoriteClick(DatabaseDelegate favoriteDelegate);

    void detailsMealClick(DatabaseDelegate favoriteDelegate);

    void todayMealAddToPlanClicked(String dayID, DatabaseDelegate databaseDelegate);

    Completable insertMealToPlan(SimpleMeal simpleMeal);

    void getAllPlansOfDay(String dayID, PlanDelegate planDelegate);

    Flowable<List<PlanModel>> getAllPlansOfDay(String dayID);

    void insertPlan(PlanModel planModel, SimpleMeal simpleMeal, DatabaseDelegate databaseDelegate);

    void insertDetailsMealToPlan(String dayID, DatabaseDelegate databaseDelegate);

    Single<SimpleMeal> getPlanMealWithID(String id);

    void getAllPlansByDayId(String dayID, PlanDelegate planDelegate);

    void getAllPlans(PlanDelegate planDelegate);

    Completable removePlan(PlanModel planModel);

    void addMealToFavDB(Meal meal, DatabaseDelegate databaseDelegate);

    void removeMealFromFavDB(Meal meal, DatabaseDelegate databaseDelegate);

    Flowable<List<Meal>> getFavDBContent(String userId);

    Completable deleteAllFavorite();

    String sendTodayMealId();
}
