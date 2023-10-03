package com.project.foodplanner.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.project.foodplanner.model.Country;
import com.project.foodplanner.model.Ingredient;
import com.project.foodplanner.model.IngredientResponse;
import com.project.foodplanner.model.CategoryResponse;
import com.project.foodplanner.model.MealResponse;
import com.project.foodplanner.model.RequestCode;
import com.project.foodplanner.model.ResponseModel;

import java.util.ArrayList;
import java.util.List;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealClient implements RemoteSource {
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    public static final String TAG = "TAG meals client";
    private static MealClient instance = null;

    public static MealClient getInstance() {
        if (instance == null)
            instance = new MealClient();
        return instance;
    }

    @Override
    public void makeCategoryListCall(NetworkCallback networkCallback) {
        makeNetworkCall().getCategoryList().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    //Log.i(TAG, "onSuccess: " + response.raw() + "categories: " + response.body().get("categories"));
                    networkCallback.onSuccessResult(RequestCode.CATEGORIES_REQ, response.body());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                networkCallback.onFailureResult(RequestCode.CATEGORIES_REQ, t.getMessage());
                Log.i(TAG, "onFailure: " + t.getCause());
            }
        });
    }

    @Override
    public void makeCountryListCall(NetworkCallback networkCallback) {
        Log.i(TAG, "makeCountryListCall: ");
        makeNetworkCall().getCountryList().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i(TAG, "onResponse: countries: " + response.body().get("meals"));
                    networkCallback.onSuccessResult(RequestCode.COUNTRIES_REQ, response.body());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                networkCallback.onFailureResult(RequestCode.COUNTRIES_REQ, t.getMessage());
            }
        });
    }

    @Override
    public void makeIngredientListCall(NetworkCallback networkCallback) {
        makeNetworkCall().getIngredientList().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    //Log.i(TAG, "onSuccess: " + response.raw() + "ingredients: " + response.body().get("meals"));
                    networkCallback.onSuccessResult(RequestCode.INGREDIENTS_REQ, response.body());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                networkCallback.onFailureResult(RequestCode.INGREDIENTS_REQ, t.getMessage());
            }
        });
    }

    @Override
    public void makeRandomMealCall(NetworkCallback networkCallback) {
        makeNetworkCall().getRandomMeal().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    //Log.i(TAG, "onSuccess: " + response.body().get("meals"));
                    networkCallback.onSuccessResult(RequestCode.RANDOM_MEAL_REQ, response.body());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                networkCallback.onFailureResult(RequestCode.RANDOM_MEAL_REQ, t.getMessage());
            }
        });
    }

    @Override
    public void searchByFirstCharacterCall(char charToSearchWith, NetworkCallback networkCallback) {
        makeNetworkCall().getMealsWithFirstLetter(charToSearchWith).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i(TAG, "onSuccess: meals with char " + charToSearchWith + ": " + response.body().get("meals"));
                    networkCallback.onSuccessResult(RequestCode.MEAL_BY_CHAR, response.body());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                networkCallback.onFailureResult(RequestCode.MEAL_BY_CHAR, t.getMessage());
            }
        });
    }

    @Override
    public Single<MealResponse> filterByIngredient(String ingredient) {
        return makeNetworkCall().filterByIngredient(ingredient)
                .doOnSubscribe(sub -> Log.i(TAG, "filterByIngredient: subscribe"))
                .doOnError(error -> Log.i(TAG, "filterByIngredient: error" + error.getMessage()))
                .doOnSuccess(success -> Log.i(TAG, "filterByIngredient: success"));
    }

    @Override
    public Single<MealResponse> filterByCategory(String category) {
        return makeNetworkCall().filterByCategory(category)
                .doOnSubscribe(sub -> Log.i(TAG, "filterByCategory: subscribe"))
                .doOnError(error -> Log.i(TAG, "filterByCategory: error" + error.getMessage()))
                .doOnSuccess(success -> Log.i(TAG, "filterByCategory: success"));
    }

    @Override
    public Single<MealResponse> filterByCountry(String country) {
        return makeNetworkCall().filterByCountry(country)
                .doOnSubscribe(sub -> Log.i(TAG, "filterByCountry: subscribe"))
                .doOnError(error -> Log.i(TAG, "filterByCountry: error" + error.getMessage()))
                .doOnSuccess(success -> Log.i(TAG, "filterByCountry: success"));
    }

    public MealService makeNetworkCall() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        return retrofit.create(MealService.class);
    }
}
