package com.project.foodplanner.favorite.presenter;

import androidx.lifecycle.LiveData;

import com.project.foodplanner.favorite.view.FavoriteViewInterface;
import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.MealsRepositoryInterface;
import com.project.foodplanner.network.DatabaseDelegate;

import java.util.List;

public class FavoritePresenter implements FavoritePresenterInterface {
    private static final String TAG = "TAG favorite presenter";
    FavoriteViewInterface view;
    MealsRepositoryInterface repository;

    public FavoritePresenter(FavoriteViewInterface view, MealsRepositoryInterface repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public LiveData<List<Meal>> getAllFavMeals() {
        return repository.getDatabaseContent();
    }

    @Override
    public void removeMeal(Meal meal) {
        repository.removeMealFromDatabase(meal, new DatabaseDelegate() {
            @Override
            public void onSuccess(String mealName, int Status) {
                view.showRemoveMealMessage(mealName);
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
