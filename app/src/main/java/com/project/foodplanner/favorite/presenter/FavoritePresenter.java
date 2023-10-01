package com.project.foodplanner.favorite.presenter;

import androidx.lifecycle.LiveData;

import com.project.foodplanner.favorite.view.FavoriteViewInterface;
import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.RepositoryInterface;

import java.util.List;

public class FavoritePresenter implements FavoritePresenterInterface {
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
        repository.removeMealFromDatabase(meal);
    }
}
