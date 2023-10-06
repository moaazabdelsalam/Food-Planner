package com.project.foodplanner.plan.presenter;

import android.util.Log;

import com.project.foodplanner.database.PlanDelegate;
import com.project.foodplanner.model.RepositoryInterface;
import com.project.foodplanner.model.SimpleMeal;
import com.project.foodplanner.plan.view.PlanViewInterface;

import java.util.List;

public class PlanPresenter implements PlanPresenterInterface {
    public static final String TAG = "TAG plan presenter";
    PlanViewInterface view;
    RepositoryInterface repository;

    public PlanPresenter(PlanViewInterface view, RepositoryInterface repository) {
        this.view = view;
        this.repository = repository;
    }


    @Override
    public void getPlanWithId(String dayID) {
        Log.i(TAG, "getPlanWithId: requesting plans of day: " + dayID);
        repository.getAllPlansByDayId(dayID, new PlanDelegate() {
            @Override
            public void onSuccess(SimpleMeal simpleMeal, String dayID) {
                Log.i(TAG, "onSuccess: result plan size: " + simpleMeal.getStrMeal());
                view.updateAdapterWithMeal(dayID, simpleMeal);
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
