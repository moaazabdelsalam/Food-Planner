package com.project.foodplanner.network;

public interface DatabaseDelegate {
    void onSuccess(String mealName, int status);

    void onError(String error);
}
