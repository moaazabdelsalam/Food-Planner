package com.project.foodplanner.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.project.foodplanner.model.Meal;

@Database(entities = {Meal.class}, version = 1)
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
