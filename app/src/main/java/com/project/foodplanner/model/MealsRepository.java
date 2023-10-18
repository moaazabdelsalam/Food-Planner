package com.project.foodplanner.model;

import android.util.Log;

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
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealsRepository implements MealsRepositoryInterface {
    private static final String TAG = "TAG repository";
    CloudRepoInterface cloudRepo;
    RemoteSource remoteSource;
    LocalSource localSource;
    private static MealsRepository instance = null;
    DummyCache cache = DummyCache.getInstance();

    private MealsRepository(RemoteSource remoteSource, LocalSource localSource) {
        this.remoteSource = remoteSource;
        this.localSource = localSource;
        this.cloudRepo = CloudRepo.getInstance(this);
    }

    public static MealsRepository getInstance(RemoteSource remoteSource, LocalSource localSource) {
        if (instance == null)
            instance = new MealsRepository(remoteSource, localSource);
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
    public void filterByCountry(String country, NetworkDelegate networkDelegate) {
        if (cache.getFilterResultMealCache() != null) {
            //Log.i(TAG, "filterByCountry: result: " + cache.getFilterResultMealCache());
            networkDelegate.onSuccess(cache.getRegionMealsCache());
        } else remoteSource.filterByCountry(country)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealResponse -> {
                            networkDelegate.onSuccess(mealResponse.getMeals());
                            cache.setFilterResultMealCache(mealResponse.getMeals());
                        },
                        error -> networkDelegate.onError(error.getMessage())
                );
    }

    @Override
    public void getRegionMeals(String country, NetworkDelegate networkDelegate) {
        if (cache.getRegionMealsCache() != null) {
            //Log.i(TAG, "filterByCountry: result: " + cache.getFilterResultMealCache());
            networkDelegate.onSuccess(cache.getRegionMealsCache());
        } else remoteSource.filterByCountry(country)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealResponse -> {
                            networkDelegate.onSuccess(mealResponse.getMeals());
                            cache.setRegionMealsCache(mealResponse.getMeals());
                        },
                        error -> networkDelegate.onError(error.getMessage())
                );
    }

    @Override
    public void getMealById(String mealId, NetworkDelegate networkDelegate) {
        final ArrayList<Meal> _meal = new ArrayList<>();
        cache.getMealOnDetailsCache().stream().filter(meal -> meal.getIdMeal().equals(mealId)).findAny().ifPresent(_meal::add);
        if (!_meal.isEmpty()) {
            //Log.i(TAG, "getMealById: getting meal local " + _meal);
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
        //Log.i(TAG, "todayMealFavoriteClick: " + cache.getTodayMealCache().getStrMeal());
        if (!cache.getTodayMealCache().isFavorite()) {
            //Log.i(TAG, "todayMealFavoriteClick: adding to favorite");
            cache.getTodayMealCache().setUserID(cloudRepo.getCurrentUser().getUid());
            localSource.addMealToFav(cache.getTodayMealCache())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
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
            //Log.i(TAG, "todayMealFavoriteClick: removing from favorite");
            cache.getTodayMealCache().setUserID(cloudRepo.getCurrentUser().getUid());
            localSource.removeMealFromFav(cache.getTodayMealCache())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
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
        Meal meal = cache.getMealOnDetailsCache().get(cache.getMealOnDetailsCache().size() - 1);
        meal.setUserID(cloudRepo.getCurrentUser().getUid());
        if (!meal.isFavorite()) {
            //Log.i(TAG, "detailsMealClick: adding to favorite");
            localSource.addMealToFav(meal)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
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
            //Log.i(TAG, "todayMealFavoriteClick: removing from favorite");
            localSource.removeMealFromFav(meal)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
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
        meal.setUserID(cloudRepo.getCurrentUser().getUid());
        //Log.i(TAG, "todayMealAddToPlanClicked: adding meal: " + meal.getStrMeal());
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
        //Log.i(TAG, "insertMealToPlan: inserting simple meal: " + simpleMeal.getStrMeal());
        return localSource.insertMealToPlan(simpleMeal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void getAllPlansOfDay(String dayID, PlanDelegate planDelegate) {
        Log.i(TAG, "getAllPlansOfDay: " + dayID);
        localSource.getAllPlansById(cloudRepo.getCurrentUser().getUid(), dayID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        planModelList ->
                                planModelList.forEach(planModel -> {
                                    Log.i(TAG, "getting plans of day: " + planModel.getDayID());
                                    getPlanMealWithID(planModel.getIdMeal()).subscribe(
                                            simpleMeal -> {
                                                Log.i(TAG, "getAllPlansOfDay:sending simple meal " + simpleMeal.getStrMeal());
                                                planDelegate.onSuccess(simpleMeal, planModel.getDayID());
                                            },
                                            error -> Log.i(TAG, "getAllPlansById: getPlanMealWithID: error: " + error.getMessage())
                                    );
                                })
                );
    }

    @Override
    public Flowable<List<PlanModel>> getAllPlansOfDay(String dayID) {
        return localSource.getAllPlansById(cloudRepo.getCurrentUser().getUid(), dayID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void insertPlan(PlanModel planModel, SimpleMeal simpleMeal, DatabaseDelegate databaseDelegate) {
        //Log.i(TAG, "insertPlan: inserting plan: " + planModel.getIdMeal());
        planModel.setUserID(cloudRepo.getCurrentUser().getUid());
        insertMealToPlan(simpleMeal).subscribe(
                () -> {
                    //Log.i(TAG, "insertMealToPlan: success" + simpleMeal.getStrMeal() + ", id: " + simpleMeal.getIdMeal());
                    localSource.insertPlan(planModel)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    () -> {
                                        //Log.i(TAG, "insertPlan: success: " + "day: " + planModel.getDayID() + ", meal: " + planModel.getIdMeal());
                                        databaseDelegate.onSuccess(simpleMeal.getStrMeal(), 1);
                                    },
                                    error -> Log.i(TAG, "insertPlan: error: " + error.getMessage())
                            );
                },
                error -> Log.i(TAG, "insertMealToPlan: error " + error.getMessage())
        );
    }

    @Override
    public void insertDetailsMealToPlan(String dayID, DatabaseDelegate databaseDelegate) {
        Meal meal = cache.getMealOnDetailsCache().get(cache.getMealOnDetailsCache().size() - 1);
        meal.setUserID(cloudRepo.getCurrentUser().getUid());
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
    public Single<SimpleMeal> getPlanMealWithID(String id) {
        return localSource.getPlanMealWithID(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void getAllPlansByDayId(String dayID, PlanDelegate planDelegate) {
        //Log.i(TAG, "getAllPlansByDayId: day: " + dayID);
        Disposable disposable = localSource.getAllPlansById(cloudRepo.getCurrentUser().getUid(), dayID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .delay(300, TimeUnit.MILLISECONDS)
                .subscribe(
                        planModelList -> {
                            //Log.i(TAG, "getAllPlansByDayId: size: " + planModelList.size());
                            planModelList.forEach(planModel -> {
                                //Log.i(TAG, "getAllPlansByDayId: meal id: " + planModel.getIdMeal());
                                getPlanMealWithID(planModel.getIdMeal()).subscribe(
                                        simpleMeal -> {
                                            //Log.i(TAG, "getAllPlansByDayId:sending simple meal " + simpleMeal.getStrMeal());
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
        Disposable disposable = localSource.getAllPlans(cloudRepo.getCurrentUser().getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        planModelList ->
                                planModelList.forEach(planModel -> {
                                    //Log.i(TAG, "getAllPlans: got plan of day: " + planModel.getDayID());
                                    getPlanMealWithID(planModel.getIdMeal()).subscribe(
                                            simpleMeal -> {
                                                //Log.i(TAG, "getAllPlansByDayId:sending simple meal " + simpleMeal.getStrMeal());
                                                planDelegate.onSuccess(simpleMeal, planModel.getDayID());
                                            },
                                            error -> Log.i(TAG, "getAllPlansById: getPlanMealWithID: error: " + error.getMessage())
                                    );
                                })
                );
    }

    @Override
    public Completable removePlan(PlanModel planModel) {
        return localSource.deletePlan(planModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public String sendTodayMealId() {
        return cache.getTodayMealCache().getIdMeal();
    }

    @Override
    public void addMealToFavDB(Meal meal, DatabaseDelegate databaseDelegate) {
        meal.setUserID(cloudRepo.getCurrentUser().getUid());
        localSource.addMealToFav(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            cloudRepo.addFavMealToRemoteDB(meal);
                            databaseDelegate.onSuccess(meal.getStrMeal(), 1);
                        },
                        error -> {
                            databaseDelegate.onError(error.getMessage());
                            Log.i(TAG, "todayMealFavoriteClick: error" + error.getMessage());
                        }
                );
    }

    @Override
    public void removeMealFromFavDB(Meal meal, DatabaseDelegate databaseDelegate) {
        localSource.removeMealFromFav(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            databaseDelegate.onSuccess(meal.getStrMeal(), 0);
                        },
                        error -> {
                            databaseDelegate.onError(error.getMessage());
                            Log.i(TAG, "todayMealFavoriteClick: error" + error.getMessage());
                        }
                );
    }

    @Override
    public Flowable<List<Meal>> getFavDBContent(String userID) {
        return localSource.getFavMealList(userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Completable deleteAllFavorite() {
        return localSource.deleteAllFavorite();
    }
}
