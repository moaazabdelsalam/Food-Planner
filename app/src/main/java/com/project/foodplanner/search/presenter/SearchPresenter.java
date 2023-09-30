package com.project.foodplanner.search.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.project.foodplanner.model.Category;
import com.project.foodplanner.model.Ingredient;
import com.project.foodplanner.model.RepositoryInterface;
import com.project.foodplanner.model.RequestCode;
import com.project.foodplanner.network.NetworkCallback;
import com.project.foodplanner.search.view.SearchViewInterface;

import java.util.List;

public class SearchPresenter implements SearchPresenterInterface, NetworkCallback {
    private static final String TAG = "TAG search presenter";
    private SearchViewInterface view;
    private RepositoryInterface repository;

    public SearchPresenter(SearchViewInterface view, RepositoryInterface repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void getAllCategories() {
        repository.makeCategoryListCall(this);
    }

    @Override
    public void getAllIngredients() {
        repository.makeIngredientListCall(this);
    }

    @Override
    public void onSuccessResult(RequestCode requestCode, JsonObject jsonObject) {
        switch (requestCode) {
            case CATEGORIES_REQ:
                TypeToken<List<Category>> categoryTypeToken = new TypeToken<List<Category>>() {
                };
                List<Category> categoryList = new Gson().fromJson(jsonObject.get("categories"), categoryTypeToken.getType());
                view.showCategoryList(categoryList);
                break;
            case INGREDIENTS_REQ:
                TypeToken<List<Ingredient>> IngredientTypeToken = new TypeToken<List<Ingredient>>() {
                };
                List<Ingredient> ingredientList = new Gson().fromJson(jsonObject.get("meals"), IngredientTypeToken.getType());
                view.showIngredientList(ingredientList);
                break;
        }
    }

    @Override
    public void onFailureResult(String errorMsg) {
        Log.i(TAG, "onFailureResult: " + errorMsg);
    }

}
