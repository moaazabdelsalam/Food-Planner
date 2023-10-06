package com.project.foodplanner.model;

import androidx.lifecycle.LiveData;

import com.project.foodplanner.database.PlanDelegate;
import com.project.foodplanner.network.DatabaseDelegate;
import com.project.foodplanner.network.NetworkCallback;
import com.project.foodplanner.network.NetworkDelegate;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface RepositoryInterface {
    void makeCategoryListCall(NetworkCallback networkCallback);

    void makeCountryListCall(NetworkCallback networkCallback);

    void makeIngredientListCall(NetworkCallback networkCallback);

    void makeRandomMealCall(NetworkCallback networkCallback);

    Single<MealResponse> searchByFirstCharacterCall(char charToSearchWith);

    Single<MealResponse> filterByIngredient(String ingredient);

    Single<MealResponse> filterByCategory(String category);

    Single<MealResponse> filterByCountry(String country);

    void getMealById(String mealId, NetworkDelegate networkDelegate);

    void todayMealFavoriteClick(DatabaseDelegate favoriteDelegate);

    void detailsMealClick(DatabaseDelegate favoriteDelegate);

    void todayMealAddToPlanClicked(String dayID, DatabaseDelegate databaseDelegate);

    Completable insertMealToPlan(SimpleMeal simpleMeal);

    void getAllMealsOfDay(String dayID);

    void insertPlan(PlanModel planModel, SimpleMeal simpleMeal, DatabaseDelegate databaseDelegate);

    Single<SimpleMeal> getPlanMealWithID(String id);

    void getAllPlansByDayId(String dayID, PlanDelegate planDelegate);

    void getAllPlans(PlanDelegate planDelegate);

    Completable addMealToDatabase(Meal meal);

    Completable removeMealFromDatabase(Meal meal);

    LiveData<List<Meal>> getDatabaseContent();

    String sendTodayMealId();
}
