package com.project.foodplanner.model;

import com.project.foodplanner.network.NetworkCallback;

public interface RepositoryInterface {
    void makeCategoryListCall(NetworkCallback networkCallback);
    void makeIngredientListCall(NetworkCallback networkCallback);
    void makeRandomMealCall(NetworkCallback networkCallback);
}
