package com.project.foodplanner.database;

import androidx.lifecycle.LiveData;

import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.PlanModel;
import com.project.foodplanner.model.SimpleMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface LocalSource {
    Completable addMealToFav(Meal meal);

    Completable removeMealFromFav(Meal meal);

    Flowable<List<Meal>> getFavMealList(String userId);

    Completable deleteAllFavorite();

    Completable insertMealToPlan(SimpleMeal simpleMeal);

    Completable insertPlan(PlanModel planModel);

    Single<SimpleMeal> getPlanMealWithID(String id);

    Flowable<List<PlanModel>> getAllPlansById(String userID, String dayID);

    Flowable<List<PlanModel>> getAllPlans(String userID);

    Completable deletePlan(PlanModel planModel);
}
