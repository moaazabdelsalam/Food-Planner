package com.project.foodplanner.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
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

@Dao
public interface MealDAO {
    @Query("SELECT * FROM favorite_meals")
    LiveData<List<Meal>> getAllMeals();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertMeal(Meal meal);

    @Delete
    Completable deleteMeal(Meal meal);



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertMealToPlan(SimpleMeal simpleMeal);

    @Delete
    Completable deleteMealFormPlan(SimpleMeal simpleMeal);

    @Query("SELECT * FROM plan_meals WHERE idMeal = :id")
    Flowable<SimpleMeal> getPlanMealWithID(String id);



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertPlan(PlanModel planModel);

    @Query("SELECT * FROM plan_table WHERE dayID = :dayID")
    Flowable<List<PlanModel>> getAllPlansById(String dayID);

    @Query("SELECT * FROM plan_table")
    Flowable<List<PlanModel>> getAllPlans();
}