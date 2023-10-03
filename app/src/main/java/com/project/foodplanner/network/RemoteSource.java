package com.project.foodplanner.network;

public interface RemoteSource {
    void makeCategoryListCall(NetworkCallback networkCallback);

    void makeCountryListCall(NetworkCallback networkCallback);

    void makeIngredientListCall(NetworkCallback networkCallback);

    void makeRandomMealCall(NetworkCallback networkCallback);

    void searchByFirstCharacterCall(char charToSearchWith, NetworkCallback networkCallback);
}
