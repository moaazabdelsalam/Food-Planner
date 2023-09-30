package com.project.foodplanner.network;

import com.google.gson.JsonObject;
import com.project.foodplanner.model.IngredientResponse;
import com.project.foodplanner.model.CategoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MealService {
    @GET("categories.php")
    Call<JsonObject> getCategoryList();

    @GET("list.php?i=list")
    Call<JsonObject> getIngredientList();

    @GET("random.php")
    Call<JsonObject> getRandomMeal();
}
