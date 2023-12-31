package com.project.foodplanner.plan.presenter;

import com.google.firebase.auth.FirebaseUser;
import com.project.foodplanner.database.PlanDelegate;

public interface PlanPresenterInterface {

    void getPlanWithId(String dayID);

    void getAllPlans();

    void removePlan(String dayID, String mealID);

    FirebaseUser getCurrentUser();
}