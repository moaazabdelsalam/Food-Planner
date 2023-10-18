package com.project.foodplanner.database;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.PlanModel;
import com.project.foodplanner.model.SimpleMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class ConcreteLocalSource implements LocalSource {
    private Context context;
    private static ConcreteLocalSource instance = null;
    private MealDatabase mealDatabase;
    private MealDAO mealDAO;

    private ConcreteLocalSource(Context context) {
        this.context = context;
        mealDatabase = MealDatabase.getInstance(context);
        mealDAO = mealDatabase.getMealDAO();
    }

    public static ConcreteLocalSource getInstance(Context context) {
        if (instance == null) return new ConcreteLocalSource(context);
        return instance;
    }

    @Override
    public Completable addMealToFav(Meal meal) {
        return mealDAO.insertMealToFav(meal);
    }

    @Override
    public Completable removeMealFromFav(Meal meal) {
        return mealDAO.deleteMealFromFav(meal);
    }

    @Override
    public Flowable<List<Meal>> getFavMealList(String userId) {
        return mealDAO.getAllFavMeals(userId);
    }

    @Override
    public Completable deleteAllFavorite() {
        return mealDAO.deleteAllFavorite();
    }

    @Override
    public Completable insertMealToPlan(SimpleMeal simpleMeal) {
        return mealDAO.insertMealToPlan(simpleMeal);
    }

    @Override
    public Completable insertPlan(PlanModel planModel) {
        return mealDAO.insertPlan(planModel);
    }

    @Override
    public Single<SimpleMeal> getPlanMealWithID(String id) {
        return mealDAO.getPlanMealWithID(id);
    }

    @Override
    public Flowable<List<PlanModel>> getAllPlansById(String userID, String dayID) {
        return mealDAO.getAllPlansById(userID, dayID);
    }

    @Override
    public Flowable<List<PlanModel>> getAllPlans(String userID) {
        return mealDAO.getAllPlans(userID);
    }

    @Override
    public Completable deletePlan(PlanModel planModel) {
        //Log.i("TAG concrete local source", "deletePlan: of day " + planModel.getDayID() + " ,meal " + planModel.getIdMeal());
        return mealDAO.deletePlan(planModel);
    }
}