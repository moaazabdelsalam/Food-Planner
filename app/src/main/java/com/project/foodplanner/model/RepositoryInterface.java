package com.project.foodplanner.model;

import androidx.lifecycle.LiveData;

import com.project.foodplanner.network.NetworkCallback;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Query;

public interface RepositoryInterface {
    void makeCategoryListCall(NetworkCallback networkCallback);

    void makeCountryListCall(NetworkCallback networkCallback);

    void makeIngredientListCall(NetworkCallback networkCallback);

    void makeRandomMealCall(NetworkCallback networkCallback);

    Single<MealResponse> searchByFirstCharacterCall(char charToSearchWith);

    Single<MealResponse> filterByIngredient(String ingredient);

    Single<MealResponse> filterByCategory(String category);

    Single<MealResponse> filterByCountry(String country);

    Single<MealResponse> getMealById(String mealId);

    void addMealToDatabase(Meal meal);

    void removeMealFromDatabase(Meal meal);

    LiveData<List<Meal>> getDatabaseContent();
}
