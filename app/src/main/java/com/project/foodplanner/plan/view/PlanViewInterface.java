package com.project.foodplanner.plan.view;

import com.project.foodplanner.model.SimpleMeal;

import java.util.List;

public interface PlanViewInterface {
    void updateAdapterWithMeal(String tabId, SimpleMeal planSimpleMeal);

    void resetAdapterList(String dayId);

    void showNotLoggedInMessage();
}
