package com.project.foodplanner.model;

import com.project.foodplanner.network.NetworkCallback;
import com.project.foodplanner.network.RemoteSource;

public class Repository implements RepositoryInterface {
    RemoteSource remoteSource;
    private static Repository instance = null;

    private Repository(RemoteSource remoteSource) {
        this.remoteSource = remoteSource;
    }

    public static Repository getInstance(RemoteSource remoteSource) {
        if (instance == null) return new Repository(remoteSource);
        return instance;
    }

    @Override
    public void makeCategoryListCall(NetworkCallback networkCallback) {
        remoteSource.makeCategoryListCall(networkCallback);
    }

    @Override
    public void makeIngredientListCall(NetworkCallback networkCallback) {
        remoteSource.makeIngredientListCall(networkCallback);
    }

    @Override
    public void makeRandomMealCall(NetworkCallback networkCallback) {
        remoteSource.makeRandomMealCall(networkCallback);
    }
}
