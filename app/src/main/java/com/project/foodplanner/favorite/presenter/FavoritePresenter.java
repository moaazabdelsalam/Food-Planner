package com.project.foodplanner.favorite.presenter;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.project.foodplanner.favorite.view.FavoriteViewInterface;
import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.RepositoryInterface;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritePresenter implements FavoritePresenterInterface {
    private static final String TAG = "TAG favorite presenter";
    FavoriteViewInterface view;
    RepositoryInterface repository;

    public FavoritePresenter(FavoriteViewInterface view, RepositoryInterface repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public LiveData<List<Meal>> getAllFavMeals() {
        return repository.getDatabaseContent();
    }

    @Override
    public void removeMeal(Meal meal) {
        repository.removeMealFromDatabase(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            Log.i(TAG, "removeMeal: " + meal.getStrMeal() + " done");
                        },
                        error -> Log.i(TAG, "removeMeal: error" + error.getMessage())
                );
    }
}
