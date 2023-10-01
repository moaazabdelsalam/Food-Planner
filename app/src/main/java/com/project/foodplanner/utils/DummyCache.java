package com.project.foodplanner.utils;

import com.project.foodplanner.model.Category;
import com.project.foodplanner.model.Ingredient;
import com.project.foodplanner.model.Meal;

import java.util.List;

public class DummyCache {
    private static DummyCache instance = null;
    private List<Category> categoryCache;
    private List<Ingredient> ingredientCache;
    private Meal todayMealCache;

    private DummyCache() {
    }

    public static DummyCache getInstance() {
        if (instance == null)
            instance = new DummyCache();

        return instance;
    }

    public List<Category> getCategoryCache() {
        return categoryCache;
    }

    public void setCategoryCache(List<Category> categoryCache) {
        this.categoryCache = categoryCache;
    }

    public List<Ingredient> getIngredientCache() {
        return ingredientCache;
    }

    public void setIngredientCache(List<Ingredient> ingredientCache) {
        this.ingredientCache = ingredientCache;
    }

    public Meal getTodayMealCache() {
        return todayMealCache;
    }

    public void setTodayMealCache(Meal todayMealCache) {
        this.todayMealCache = todayMealCache;
    }
}
