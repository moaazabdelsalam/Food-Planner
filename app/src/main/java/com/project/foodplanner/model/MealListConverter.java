package com.project.foodplanner.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MealListConverter {
    Gson gson = new Gson();

    @TypeConverter
    public String listToString(List<Meal> mealList) {
        return gson.toJson(mealList);
    }

    @TypeConverter
    public List<Meal> stringToList(String mealListJson) {
        Type mealListType = new TypeToken<List<Meal>>() {
        }.getType();
        return gson.fromJson(mealListJson, mealListType);
    }
}
