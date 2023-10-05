package com.project.foodplanner.network;

public interface FavoriteDelegate {
    void onSuccess(String mealName, int Status);

    void onError(String error);
}
