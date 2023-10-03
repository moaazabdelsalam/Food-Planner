package com.project.foodplanner.filter.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.project.foodplanner.filter.view.FilterViewInterface;
import com.project.foodplanner.model.Category;
import com.project.foodplanner.model.Country;
import com.project.foodplanner.model.Ingredient;
import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.RepositoryInterface;
import com.project.foodplanner.model.RequestCode;
import com.project.foodplanner.network.NetworkCallback;
import com.project.foodplanner.utils.DummyCache;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FilterPresenter implements FilterPresenterInterface, NetworkCallback {
    private static final String TAG = "TAG filter presenter";
    private final FilterViewInterface view;
    private final RepositoryInterface repository;
    private final DummyCache cache = DummyCache.getInstance();
    char charToSearchWith = ' ';

    public FilterPresenter(FilterViewInterface view, RepositoryInterface repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void getAllCategories() {
        if (cache.getCategoryCache() != null) {
            //Log.i(TAG, "getAllCategories: " + cache.getCategoryCache());
            view.showCategoryList(cache.getCategoryCache());
            return;
        }
        repository.makeCategoryListCall(this);
    }

    @Override
    public void getAllIngredients() {
        if (cache.getIngredientCache() != null) {
            //Log.i(TAG, "getAllIngredients: " + cache.getIngredientCache());
            view.showIngredientList(cache.getIngredientCache());
            return;
        }
        repository.makeIngredientListCall(this);
    }

    @Override
    public void getAllCountries() {
        if (cache.getCountryCache() != null) {
            view.showCountryList(cache.getCountryCache());
            return;
        }
        repository.makeCountryListCall(this);
    }

    @Override
    public void filterMeals(String query) {
        Log.i(TAG, "filterMeals: first character: " + charToSearchWith);
        if (charToSearchWith != ' ' && query.charAt(0) == charToSearchWith) {
            Log.i(TAG, "onNext: filter local list which contains: " + cache.getMealCache().size());
            ArrayList<Meal> filteredMeals = cache.getMealCache().stream()
                    .filter(meal -> meal.getStrMeal().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toCollection(ArrayList::new));
            Log.i(TAG, "filterMeals: filtered list size: " + filteredMeals.size());
            view.showMealList(filteredMeals);
        } else {
            charToSearchWith = query.charAt(0);
            Log.i(TAG, "onNext: getting meals starts with: " + charToSearchWith);
            repository.searchByFirstCharacterCall(charToSearchWith, this);
        }
    }

    @Override
    public void filterCategories(String query) {
        ArrayList<Category> filteredCategories = cache.getCategoryCache().stream()
                .filter(category -> category.getStrCategory().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));
        Log.i(TAG, "filterCategories: filtered list size: " + filteredCategories.size());
        view.showCategoryList(filteredCategories);
    }

    @Override
    public void filterCountries(String query) {
        ArrayList<Country> filteredCountries = cache.getCountryCache().stream()
                .filter(country -> country.getStrArea().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));
        Log.i(TAG, "filterCountries: filtered list size: " + filteredCountries.size());
        view.showCountryList(filteredCountries);
    }

    @Override
    public void filterIngredients(String query) {
        ArrayList<Ingredient> filteredIngredients = cache.getIngredientCache().stream()
                .filter(ingredient -> ingredient.getStrIngredient().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));
        Log.i(TAG, "filterIngredients: filtered list size: " + filteredIngredients.size());
        view.showIngredientList(filteredIngredients);
    }

    @Override
    public void onSuccessResult(RequestCode requestCode, JsonObject jsonObject) {
        Gson gson = new Gson();
        switch (requestCode) {
            case CATEGORIES_REQ:
                TypeToken<List<Category>> categoryTypeToken = new TypeToken<List<Category>>() {
                };
                List<Category> categoryList = gson.fromJson(jsonObject.get("categories"), categoryTypeToken.getType());
                cache.setCategoryCache(categoryList);
                view.showCategoryList(cache.getCategoryCache());
                Log.i(TAG, "onSuccessResult: categories: " + categoryList);
                break;
            case INGREDIENTS_REQ:
                TypeToken<List<Ingredient>> ingredientTypeToken = new TypeToken<List<Ingredient>>() {
                };
                List<Ingredient> ingredientList = gson.fromJson(jsonObject.get("meals"), ingredientTypeToken.getType());
                cache.setIngredientCache(ingredientList);
                view.showIngredientList(cache.getIngredientCache());
                Log.i(TAG, "onSuccessResult: ingredients: " + ingredientList);
                break;
            case COUNTRIES_REQ:
                TypeToken<List<Country>> countryTypeToken = new TypeToken<List<Country>>() {
                };
                List<Country> countryList = gson.fromJson(jsonObject.get("meals"), countryTypeToken.getType());
                cache.setCountryCache(countryList);
                view.showCountryList(countryList);
                Log.i(TAG, "onSuccessResult: countries: " + countryList);
                break;
            case MEAL_BY_CHAR:
                TypeToken<List<Meal>> mealTypeToken = new TypeToken<List<Meal>>() {
                };
                List<Meal> mealList = gson.fromJson(jsonObject.get("meals"), mealTypeToken.getType());
                cache.setMealCache(mealList);
                view.showMealList(mealList);
                Log.i(TAG, "onSuccessResult: countries: " + mealList);
                break;
        }
    }

    @Override
    public void onFailureResult(RequestCode requestCode, String errorMsg) {
        Log.i(TAG, "onFailureResult: " + requestCode + " " + errorMsg);
    }
}
