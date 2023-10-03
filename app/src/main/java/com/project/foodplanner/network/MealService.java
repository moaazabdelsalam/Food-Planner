package com.project.foodplanner.network;

import com.google.gson.JsonObject;
import com.project.foodplanner.model.IngredientResponse;
import com.project.foodplanner.model.CategoryResponse;
import com.project.foodplanner.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealService {
    @GET("categories.php")
    Call<JsonObject> getCategoryList();

    @GET("list.php?i=list")
    Call<JsonObject> getIngredientList();

    @GET("random.php")
    Call<JsonObject> getRandomMeal();

    @GET("list.php?a=list")
    Call<JsonObject> getCountryList();

    @GET("search.php")
    Call<JsonObject> getMealsWithFirstLetter(@Query("f") char ch);
}
