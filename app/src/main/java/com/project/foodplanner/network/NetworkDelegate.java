package com.project.foodplanner.network;

import com.project.foodplanner.model.Meal;

import java.util.List;

public interface NetworkDelegate {
    void onSuccess(List<Meal> mealList);

    void onError(String error);
}
