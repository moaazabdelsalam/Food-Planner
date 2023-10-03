package com.project.foodplanner.model;

import androidx.lifecycle.LiveData;

import com.project.foodplanner.network.NetworkCallback;

import java.util.List;

public interface RepositoryInterface {
    void makeCategoryListCall(NetworkCallback networkCallback);

    void makeCountryListCall(NetworkCallback networkCallback);

    void makeIngredientListCall(NetworkCallback networkCallback);

    void makeRandomMealCall(NetworkCallback networkCallback);

    void searchByFirstCharacterCall(char charToSearchWith, NetworkCallback networkCallback);

    void addMealToDatabase(Meal meal);

    void removeMealFromDatabase(Meal meal);

    LiveData<List<Meal>> getDatabaseContent();
}
