package com.project.foodplanner.database;

import com.project.foodplanner.model.PlanModel;
import com.project.foodplanner.model.SimpleMeal;

import java.util.List;

public interface PlanDelegate {
    void onSuccess(SimpleMeal planSimpleMeal, String dayID);

    void onError(String error);
}
