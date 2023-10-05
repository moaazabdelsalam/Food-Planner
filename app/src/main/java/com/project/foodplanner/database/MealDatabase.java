package com.project.foodplanner.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.MealListConverter;
import com.project.foodplanner.model.PlanModel;
import com.project.foodplanner.model.SimpleMeal;

@Database(entities = {Meal.class, PlanModel.class, SimpleMeal.class}, version = 1)
@TypeConverters(MealListConverter.class)
public abstract class MealDatabase extends RoomDatabase {
    private static MealDatabase instance = null;

    public abstract MealDAO getMealDAO();

    public static synchronized MealDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), MealDatabase.class, "meals-db").build();
        }
        return instance;
    }
}
