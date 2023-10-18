package com.project.foodplanner.plan.presenter;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.project.foodplanner.database.PlanDelegate;
import com.project.foodplanner.model.CloudRepoInterface;
import com.project.foodplanner.model.MealsRepositoryInterface;
import com.project.foodplanner.model.SimpleMeal;
import com.project.foodplanner.plan.view.PlanViewInterface;

public class PlanPresenter implements PlanPresenterInterface, PlanDelegate {
    public static final String TAG = "TAG plan presenter";
    PlanViewInterface view;
    MealsRepositoryInterface repository;
    CloudRepoInterface cloudRepository;

    public PlanPresenter(PlanViewInterface view, MealsRepositoryInterface repository, CloudRepoInterface cloudRepository) {
        this.view = view;
        this.repository = repository;
        this.cloudRepository = cloudRepository;

        if (cloudRepository.getCurrentUser() == null)
            view.showNotLoggedInMessage();
        else
            getAllPlans();
        //Log.i(TAG, "PlanPresenter: current user: " + cloudRepository.getCurrentUser());
    }


    @Override
    public void getPlanWithId(String dayID) {
        //Log.i(TAG, "getPlanWithId: requesting plans of day: " + dayID);
        repository.getAllPlansByDayId(dayID, this);
    }

    @Override
    public void getAllPlans() {
        repository.getAllPlans(this);
    }

    @Override
    public void removePlan(String dayID, String mealID) {
        Log.i(TAG, "removePlan of day: " + dayID + ", meal: " + mealID);
        repository.getAllPlansOfDay(dayID)
                .subscribe(
                        planModelList -> {
                            Log.i(TAG, "all plans of day: " + dayID + " are: " + planModelList.size());
                            planModelList.stream()
                                    .filter(planModel -> planModel.getIdMeal().equals(mealID))
                                    .findAny()
                                    .ifPresent(planModel -> {
                                        Log.i(TAG, "removePlan: found match !!");
                                        repository.removePlan(planModel)
                                                .subscribe(
                                                        () -> {
                                                            view.resetAdapterList(dayID);
                                                            Log.i(TAG, "removePlan: done!");
                                                            getAllPlans();
                                                        },
                                                        throwable -> Log.i(TAG, "removePlan: error " + throwable.getMessage())
                                                );
                                    });
                        }
                );
    }

    @Override
    public FirebaseUser getCurrentUser() {
        return cloudRepository.getCurrentUser();
    }

    @Override
    public void onSuccess(SimpleMeal planSimpleMeal, String dayID) {
        //Log.i(TAG, "onSuccess: result plan size: " + planSimpleMeal.getStrMeal());
        view.updateAdapterWithMeal(dayID, planSimpleMeal);
    }

    @Override
    public void onError(String error) {
        Log.i(TAG, "onError: " + error);
    }
}
