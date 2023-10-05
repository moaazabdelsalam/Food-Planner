package com.project.foodplanner.database;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.PlanModel;
import com.project.foodplanner.model.SimpleMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

public interface LocalSource {
    Completable addMeal(Meal meal);

    Completable removeMeal(Meal meal);

    LiveData<List<Meal>> getFavMealList();

    Completable insertMealToPlan(SimpleMeal simpleMeal);

    Completable insertPlan(PlanModel planModel);
}
