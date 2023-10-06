package com.project.foodplanner.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.PlanModel;
import com.project.foodplanner.model.SimpleMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.subjects.PublishSubject;

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
    public Completable addMeal(Meal meal) {
        return mealDAO.insertMeal(meal);
    }

    @Override
    public Completable removeMeal(Meal meal) {
        return mealDAO.deleteMeal(meal);
    }

    @Override
    public LiveData<List<Meal>> getFavMealList() {
        return mealDAO.getAllMeals();
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
    public Flowable<List<PlanModel>> getAllPlansById(String dayID) {
        return mealDAO.getAllPlansById(dayID);
    }
}