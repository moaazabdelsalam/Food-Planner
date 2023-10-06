package com.project.foodplanner.plan.presenter;

import com.project.foodplanner.database.PlanDelegate;

public interface PlanPresenterInterface {

    void getPlanWithId(String dayID);

    void getAllPlans();
}