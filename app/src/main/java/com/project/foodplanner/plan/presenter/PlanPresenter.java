package com.project.foodplanner.plan.presenter;

import android.util.Log;

import com.project.foodplanner.database.PlanDelegate;
import com.project.foodplanner.model.MealsRepositoryInterface;
import com.project.foodplanner.model.SimpleMeal;
import com.project.foodplanner.plan.view.PlanViewInterface;

public class PlanPresenter implements PlanPresenterInterface, PlanDelegate {
    public static final String TAG = "TAG plan presenter";
    PlanViewInterface view;
    MealsRepositoryInterface repository;

    public PlanPresenter(PlanViewInterface view, MealsRepositoryInterface repository) {
        this.view = view;
        this.repository = repository;
    }


    @Override
    public void getPlanWithId(String dayID) {
        Log.i(TAG, "getPlanWithId: requesting plans of day: " + dayID);
        repository.getAllPlansByDayId(dayID, this);
    }

    @Override
    public void getAllPlans() {
        repository.getAllPlans(this);
    }

    @Override
    public void onSuccess(SimpleMeal planSimpleMeal, String dayID) {
        Log.i(TAG, "onSuccess: result plan size: " + planSimpleMeal.getStrMeal());
        view.updateAdapterWithMeal(dayID, planSimpleMeal);
    }

    @Override
    public void onError(String error) {
        Log.i(TAG, "onError: " + error);
    }
}
