package com.project.foodplanner.filterresult.presenter;

import android.util.Log;

import com.project.foodplanner.filterresult.view.FilterResultViewInterface;
import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.PlanModel;
import com.project.foodplanner.model.MealsRepositoryInterface;
import com.project.foodplanner.model.SimpleMeal;
import com.project.foodplanner.network.DatabaseDelegate;
import com.project.foodplanner.network.NetworkDelegate;
import com.project.foodplanner.utils.DummyCache;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FilterResultPresenter implements FilterResultPresenterInterface, NetworkDelegate {
    private static final String TAG = "TAG filter presenter";
    private final FilterResultViewInterface view;
    private final MealsRepositoryInterface repository;
    private final DummyCache cache = DummyCache.getInstance();

    public FilterResultPresenter(FilterResultViewInterface view, MealsRepositoryInterface repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void filterByIngredient(String ingredient) {
        if (cache.getFilterResultMealCache() != null) {
            view.updateAdapterList(cache.getFilterResultMealCache());
            return;
        }
        Log.i(TAG, "filterByIngredient: " + ingredient);
        repository.filterByIngredient(ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        item -> {
                            if (item.getMeals() != null) {
                                view.updateAdapterList(item.getMeals());
                                cache.setFilterResultMealCache(item.getMeals());
                            } else {
                                Log.i(TAG, "filterByIngredient: no meals with " + ingredient);
                                view.updateAdapterList(new ArrayList<>());
                            }
                        },
                        error -> Log.i(TAG, "filterByIngredient: error" + error.getMessage())
                );
    }

    @Override
    public void filterByCategory(String category) {
        if (cache.getFilterResultMealCache() != null) {
            view.updateAdapterList(cache.getFilterResultMealCache());
            return;
        }
        Log.i(TAG, "filterByCategory: " + category);
        repository.filterByCategory(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        item -> {
                            view.updateAdapterList(item.getMeals());
                            cache.setFilterResultMealCache(item.getMeals());
                        },
                        error -> Log.i(TAG, "filterByCategory: error" + error.getMessage())
                );
    }

    @Override
    public void filterByCountry(String country) {
        Log.i(TAG, "filterByCountry: " + country);
        repository.filterByCountry(country, this);
    }

    @Override
    public void addToFavorite(Meal meal) {
        repository.addMealToDatabase(meal, new DatabaseDelegate() {
            @Override
            public void onSuccess(String mealName, int Status) {
                view.showFavoriteClickMessage(mealName, 1);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    public void removeFromFavorite(Meal meal) {
        repository.removeMealFromDatabase(meal, new DatabaseDelegate() {
            @Override
            public void onSuccess(String mealName, int Status) {
                view.showFavoriteClickMessage(mealName, 1);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    public void addMealToPlan(Meal meal, String dayId) {
        repository.insertPlan(
                new PlanModel(dayId, meal.getIdMeal()),
                new SimpleMeal(meal.getIdMeal(),
                        meal.getStrMeal(),
                        meal.getStrCategory(),
                        meal.getStrArea(),
                        meal.getStrMealThumb(),
                        meal.getStrTags()),
                new DatabaseDelegate() {
                    @Override
                    public void onSuccess(String mealName, int status) {
                        view.showAddToPlanMessage(mealName, status);
                    }

                    @Override
                    public void onError(String error) {

                    }
                }
        );
    }

    @Override
    public void clearCache() {
        cache.clearFilterResultMealCache();
    }

    @Override
    public void onSuccess(List<Meal> mealList) {
        view.updateAdapterList(mealList);
    }

    @Override
    public void onError(String error) {

    }
}
