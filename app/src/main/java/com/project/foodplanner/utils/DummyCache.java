package com.project.foodplanner.utils;

import com.project.foodplanner.model.Category;
import com.project.foodplanner.model.Country;
import com.project.foodplanner.model.Ingredient;
import com.project.foodplanner.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.CheckedOutputStream;

public class DummyCache {
    private static DummyCache instance = null;
    private List<Category> categoryCache;
    private List<Ingredient> ingredientCache;
    private List<Country> countryCache;
    private List<Meal> mealCache;
    private List<Meal> filterResultMealCache;
    private Meal todayMealCache;
    private List<Meal> mealOnDetailsCache;

    private DummyCache() {
        mealOnDetailsCache = new ArrayList<>();
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

    public List<Country> getCountryCache() {
        return countryCache;
    }

    public void setCountryCache(List<Country> countryCache) {
        this.countryCache = countryCache;
    }

    public List<Meal> getMealCache() {
        return mealCache;
    }

    public void setMealCache(List<Meal> mealCache) {
        this.mealCache = mealCache;
    }

    public List<Meal> getFilterResultMealCache() {
        return filterResultMealCache;
    }

    public void setFilterResultMealCache(List<Meal> filterResultMealCache) {
        this.filterResultMealCache = filterResultMealCache;
    }

    public void clearFilterResultMealCache() {
        filterResultMealCache = null;
    }

    public Meal getTodayMealCache() {
        return todayMealCache;
    }

    public void setTodayMealCache(Meal todayMealCache) {
        this.todayMealCache = todayMealCache;
    }

    public List<Meal> getMealOnDetailsCache() {
        return mealOnDetailsCache;
    }

    public void addToMealOnDetailsCache(Meal meal) {
        this.mealOnDetailsCache.add(meal);
    }
}
