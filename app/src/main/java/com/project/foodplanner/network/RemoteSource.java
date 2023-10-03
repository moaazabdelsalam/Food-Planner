package com.project.foodplanner.network;

import com.project.foodplanner.model.MealResponse;
import com.project.foodplanner.model.ResponseModel;

import io.reactivex.rxjava3.core.Single;

public interface RemoteSource {
    void makeCategoryListCall(NetworkCallback networkCallback);

    void makeCountryListCall(NetworkCallback networkCallback);

    void makeIngredientListCall(NetworkCallback networkCallback);

    void makeRandomMealCall(NetworkCallback networkCallback);

    void searchByFirstCharacterCall(char charToSearchWith, NetworkCallback networkCallback);

    Single<MealResponse> filterByIngredient(String ingredient);

    Single<MealResponse> filterByCategory(String category);

    Single<MealResponse> filterByCountry(String country);
}
