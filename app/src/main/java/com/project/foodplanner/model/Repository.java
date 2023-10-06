package com.project.foodplanner.model;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.project.foodplanner.database.PlanDelegate;
import com.project.foodplanner.network.DatabaseDelegate;
import com.project.foodplanner.database.LocalSource;
import com.project.foodplanner.network.NetworkCallback;
import com.project.foodplanner.network.NetworkDelegate;
import com.project.foodplanner.network.RemoteSource;
import com.project.foodplanner.utils.DummyCache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Repository implements RepositoryInterface {
    private static final String TAG = "TAG repository";
    RemoteSource remoteSource;
    LocalSource localSource;
    private static Repository instance = null;
    DummyCache cache = DummyCache.getInstance();

    private Repository(RemoteSource remoteSource, LocalSource localSource) {
        this.remoteSource = remoteSource;
        this.localSource = localSource;
    }

    public static Repository getInstance(RemoteSource remoteSource, LocalSource localSource) {
        if (instance == null)
            instance = new Repository(remoteSource, localSource);
        return instance;
    }

    @Override
    public void makeCategoryListCall(NetworkCallback networkCallback) {
        remoteSource.makeCategoryListCall(networkCallback);
    }

    @Override
    public void makeCountryListCall(NetworkCallback networkCallback) {
        remoteSource.makeCountryListCall(networkCallback);
    }

    @Override
    public void makeIngredientListCall(NetworkCallback networkCallback) {
        remoteSource.makeIngredientListCall(networkCallback);
    }

    @Override
    public void makeRandomMealCall(NetworkCallback networkCallback) {
        remoteSource.makeRandomMealCall(networkCallback);
    }

    @Override
    public Single<MealResponse> searchByFirstCharacterCall(char charToSearchWith) {
        return remoteSource.searchByFirstCharacterCall(charToSearchWith);
    }

    @Override
    public Single<MealResponse> filterByIngredient(String ingredient) {
        return remoteSource.filterByIngredient(ingredient);
    }

    @Override
    public Single<MealResponse> filterByCategory(String category) {
        return remoteSource.filterByCategory(category);
    }

    @Override
    public Single<MealResponse> filterByCountry(String country) {
        return remoteSource.filterByCountry(country);
    }

    @Override
    public void getMealById(String mealId, NetworkDelegate networkDelegate) {
        final ArrayList<Meal> _meal = new ArrayList<>();
        cache.getMealOnDetailsCache().stream().filter(meal -> meal.getIdMeal().equals(mealId)).findAny().ifPresent(_meal::add);
        if (!_meal.isEmpty()) {
            Log.i(TAG, "getMealById: getting meal local " + _meal);
            networkDelegate.onSuccess(cache.getMealOnDetailsCache());
            return;
        }
        Log.i(TAG, "getMealById: getting meal from network ");
        remoteSource.getMealById(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        item -> {
                            cache.addToMealOnDetailsCache(item.getMeals().get(0));
                            _meal.add(item.getMeals().get(0));
                            networkDelegate.onSuccess(_meal);
                        },
                        error -> Log.i(TAG, "getMealById: error " + error.getMessage())
                );
    }

    @Override
    public void todayMealFavoriteClick(DatabaseDelegate favoriteDelegate) {
        Log.i(TAG, "todayMealFavoriteClick: " + cache.getTodayMealCache().getStrMeal());
        if (!cache.getTodayMealCache().isFavorite()) {
            Log.i(TAG, "todayMealFavoriteClick: adding to favorite");
            localSource.addMeal(cache.getTodayMealCache())
                    .subscribeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> {
                                favoriteDelegate.onSuccess(cache.getTodayMealCache().getStrMeal(), 1);
                                cache.getTodayMealCache().setFavorite(true);
                            },
                            error -> {
                                favoriteDelegate.onError(error.getMessage());
                                Log.i(TAG, "todayMealFavoriteClick: error" + error.getMessage());
                            }
                    );
        } else {
            Log.i(TAG, "todayMealFavoriteClick: removing from favorite");
            localSource.removeMeal(cache.getTodayMealCache())
                    .subscribeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> {
                                favoriteDelegate.onSuccess(cache.getTodayMealCache().getStrMeal(), 0);
                                cache.getTodayMealCache().setFavorite(true);
                            },
                            error -> {
                                favoriteDelegate.onError(error.getMessage());
                                Log.i(TAG, "todayMealFavoriteClick: error" + error.getMessage());
                            }
                    );
        }
    }

    @Override
    public void detailsMealClick(DatabaseDelegate favoriteDelegate) {
        if (!cache.getMealOnDetailsCache().get(cache.getMealOnDetailsCache().size() - 1).isFavorite()) {
            Log.i(TAG, "detailsMealClick: adding to favorite");
            localSource.addMeal(cache.getMealOnDetailsCache().get(cache.getMealOnDetailsCache().size() - 1))
                    .subscribeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> {
                                favoriteDelegate.onSuccess(cache.getMealOnDetailsCache().get(cache.getMealOnDetailsCache().size() - 1).getStrMeal(), 1);
                                cache.getMealOnDetailsCache().get(cache.getMealOnDetailsCache().size() - 1).setFavorite(true);
                            },
                            error -> {
                                favoriteDelegate.onError(error.getMessage());
                                Log.i(TAG, "todayMealFavoriteClick: error" + error.getMessage());
                            }
                    );
        } else {
            Log.i(TAG, "todayMealFavoriteClick: removing from favorite");
            localSource.removeMeal(cache.getMealOnDetailsCache().get(cache.getMealOnDetailsCache().size() - 1))
                    .subscribeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            () -> {
                                favoriteDelegate.onSuccess(cache.getMealOnDetailsCache().get(cache.getMealOnDetailsCache().size() - 1).getStrMeal(), 0);
                                cache.getMealOnDetailsCache().get(cache.getMealOnDetailsCache().size() - 1).setFavorite(true);
                            },
                            error -> {
                                favoriteDelegate.onError(error.getMessage());
                                Log.i(TAG, "todayMealFavoriteClick: error" + error.getMessage());
                            }
                    );
        }
    }

    @Override
    public void todayMealAddToPlanClicked(String dayID, DatabaseDelegate databaseDelegate) {
        Meal meal = DummyCache.getInstance().getTodayMealCache();
        Log.i(TAG, "todayMealAddToPlanClicked: adding meal: " + meal.getStrMeal());
        insertPlan(
                new PlanModel(
                        dayID,
                        meal.getIdMeal()
                ),
                new SimpleMeal(
                        meal.getIdMeal(),
                        meal.getStrMeal(),
                        meal.getStrCategory(),
                        meal.getStrArea(),
                        meal.getStrMealThumb(),
                        meal.getStrTags()
                ),
                databaseDelegate
        );
    }

    @Override
    public Completable insertMealToPlan(SimpleMeal simpleMeal) {
        Log.i(TAG, "insertMealToPlan: inserting simple meal: " + simpleMeal.getStrMeal());
        return localSource.insertMealToPlan(simpleMeal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void getAllMealsOfDay(String dayID) {

    }

    @Override
    public void insertPlan(PlanModel planModel, SimpleMeal simpleMeal, DatabaseDelegate databaseDelegate) {
        Log.i(TAG, "insertPlan: inserting plan: " + planModel.getIdMeal());
        insertMealToPlan(simpleMeal).subscribe(
                () -> {
                    Log.i(TAG, "insertMealToPlan: success" + simpleMeal.getStrMeal() + ", id: " + simpleMeal.getIdMeal());
                    localSource.insertPlan(planModel)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    () -> {
                                        Log.i(TAG, "insertPlan: success: " + "day: " + planModel.getDayID() + ", meal: " + planModel.getIdMeal());
                                        databaseDelegate.onSuccess(simpleMeal.getStrMeal(), 1);
                                    },
                                    error -> Log.i(TAG, "insertPlan: error: " + error.getMessage())
                            );
                },
                error -> Log.i(TAG, "insertMealToPlan: error " + error.getMessage())
        );
    }

    @Override
    public Single<SimpleMeal> getPlanMealWithID(String id) {
        return localSource.getPlanMealWithID(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void getAllPlansByDayId(String dayID, PlanDelegate planDelegate) {
        Log.i(TAG, "getAllPlansByDayId: day: " + dayID);
        Disposable disposable = localSource.getAllPlansById(dayID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .delay(300, TimeUnit.MILLISECONDS)
                .subscribe(
                        planModelList -> {
                            Log.i(TAG, "getAllPlansByDayId: size: " + planModelList.size());
                            planModelList.forEach(planModel -> {
                                Log.i(TAG, "getAllPlansByDayId: meal id: " + planModel.getIdMeal());
                                getPlanMealWithID(planModel.getIdMeal()).subscribe(
                                        simpleMeal -> {
                                            Log.i(TAG, "getAllPlansByDayId:sending simple meal " + simpleMeal.getStrMeal());
                                            planDelegate.onSuccess(simpleMeal, dayID);
                                        },
                                        error -> Log.i(TAG, "getAllPlansById: getPlanMealWithID: error: " + error.getMessage())
                                );
                            });
                        }
                );
    }

    @Override
    public void getAllPlans(PlanDelegate planDelegate) {
        Disposable disposable = localSource.getAllPlans()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        planModelList ->
                                planModelList.forEach(planModel -> {
                                    Log.i(TAG, "getAllPlans: got plan of day: " + planModel.getDayID());
                                    getPlanMealWithID(planModel.getIdMeal()).subscribe(
                                            simpleMeal -> {
                                                Log.i(TAG, "getAllPlansByDayId:sending simple meal " + simpleMeal.getStrMeal());
                                                planDelegate.onSuccess(simpleMeal, planModel.getDayID());
                                            },
                                            error -> Log.i(TAG, "getAllPlansById: getPlanMealWithID: error: " + error.getMessage())
                                    );
                                })
                );
    }

    @Override
    public String sendTodayMealId() {
        return cache.getTodayMealCache().getIdMeal();
    }

    @Override
    public Completable addMealToDatabase(Meal meal) {
        return localSource.addMeal(meal);
    }

    @Override
    public Completable removeMealFromDatabase(Meal meal) {
        return localSource.removeMeal(meal);
    }

    @Override
    public LiveData<List<Meal>> getDatabaseContent() {
        return localSource.getFavMealList();
    }
}
