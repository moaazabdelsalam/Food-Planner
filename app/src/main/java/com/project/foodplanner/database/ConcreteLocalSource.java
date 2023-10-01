package com.project.foodplanner.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.project.foodplanner.model.Meal;

import java.util.List;

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
    public void addMeal(Meal meal) {
        new Thread(() -> mealDAO.insertMeal(meal)).start();
    }

    @Override
    public void removeMeal(Meal meal) {
        new Thread(() -> mealDAO.deleteMeal(meal)).start();
    }

    @Override
    public LiveData<List<Meal>> getFavMealList() {
        return mealDAO.getAllMeals();
    }
}
