package com.project.foodplanner.network;

public interface DatabaseDelegate {
    void onSuccess(String mealName, int Status);

    void onError(String error);
}
