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
import io.reactivex.rxjava3.core.Single;

@Dao
public interface MealDAO {
    @Query("SELECT * FROM favorite_meals WHERE userID =:userId")
    Flowable<List<Meal>> getAllFavMeals(String userId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertMealToFav(Meal meal);

    @Delete
    Completable deleteMealFromFav(Meal meal);

    @Query("DELETE FROM favorite_meals")
    Completable deleteAllFavorite();


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertMealToPlan(SimpleMeal simpleMeal);

    @Delete
    Completable deleteMealFormPlan(SimpleMeal simpleMeal);

    @Query("SELECT * FROM plan_meals WHERE idMeal = :id")
    Single<SimpleMeal> getPlanMealWithID(String id);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertPlan(PlanModel planModel);

    @Query("SELECT * FROM plan_table WHERE userID = :userID AND dayID = :dayID")
    Flowable<List<PlanModel>> getAllPlansById(String userID, String dayID);

    @Query("SELECT * FROM plan_table WHERE userID = :userID")
    Flowable<List<PlanModel>> getAllPlans(String userID);
}