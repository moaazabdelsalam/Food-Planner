package com.project.foodplanner.model;

import androidx.lifecycle.LiveData;

import com.project.foodplanner.database.LocalSource;
import com.project.foodplanner.network.NetworkCallback;
import com.project.foodplanner.network.RemoteSource;
import com.project.foodplanner.utils.DummyCache;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class Repository implements RepositoryInterface {
    RemoteSource remoteSource;
    LocalSource localSource;
    private static Repository instance = null;
    DummyCache cache = DummyCache.getInstance();

    private Repository(RemoteSource remoteSource, LocalSource localSource) {
        this.remoteSource = remoteSource;
        this.localSource = localSource;
    }

    public static Repository getInstance(RemoteSource remoteSource, LocalSource localSource) {
        if (instance == null)
            instance = new Repository(remoteSource, localSource);
        return instance;
    }

    @Override
    public void makeCategoryListCall(NetworkCallback networkCallback) {
        remoteSource.makeCategoryListCall(networkCallback);
    }

    @Override
    public void makeCountryListCall(NetworkCallback networkCallback) {
        remoteSource.makeCountryListCall(networkCallback);
    }

    @Override
    public void makeIngredientListCall(NetworkCallback networkCallback) {
        remoteSource.makeIngredientListCall(networkCallback);
    }

    @Override
    public void makeRandomMealCall(NetworkCallback networkCallback) {
        remoteSource.makeRandomMealCall(networkCallback);
    }

    @Override
    public Single<MealResponse> searchByFirstCharacterCall(char charToSearchWith) {
        return remoteSource.searchByFirstCharacterCall(charToSearchWith);
    }

    @Override
    public Single<MealResponse> filterByIngredient(String ingredient) {
        return remoteSource.filterByIngredient(ingredient);
    }

    @Override
    public Single<MealResponse> filterByCategory(String category) {
        return remoteSource.filterByCategory(category);
    }

    @Override
    public Single<MealResponse> filterByCountry(String country) {
        return remoteSource.filterByCountry(country);
    }

    @Override
    public Single<MealResponse> getMealById(String mealId) {
        return remoteSource.getMealById(mealId);
    }

    @Override
    public void addMealToDatabase(Meal meal) {
        localSource.addMeal(meal);
    }

    @Override
    public void removeMealFromDatabase(Meal meal) {
        localSource.removeMeal(meal);
    }

    @Override
    public LiveData<List<Meal>> getDatabaseContent() {
        return localSource.getFavMealList();
    }
}
