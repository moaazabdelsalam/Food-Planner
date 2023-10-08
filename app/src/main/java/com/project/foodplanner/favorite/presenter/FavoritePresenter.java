package com.project.foodplanner.favorite.presenter;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseUser;
import com.project.foodplanner.favorite.view.FavoriteViewInterface;
import com.project.foodplanner.model.CloudRepoInterface;
import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.MealsRepositoryInterface;
import com.project.foodplanner.network.DatabaseDelegate;

import java.util.List;

public class FavoritePresenter implements FavoritePresenterInterface {
    private static final String TAG = "TAG favorite presenter";
    FavoriteViewInterface view;
    MealsRepositoryInterface repository;
    CloudRepoInterface cloudRepository;

    public FavoritePresenter(FavoriteViewInterface view, MealsRepositoryInterface repository, CloudRepoInterface cloudRepository) {
        this.view = view;
        this.repository = repository;
        this.cloudRepository = cloudRepository;

    }

    @Override
    public LiveData<List<Meal>> getAllFavMeals() {
        return repository.getFavDBContent(cloudRepository.getCurrentUser().getUid());
    }

    @Override
    public void removeMeal(Meal meal) {
        repository.removeMealFromFavDB(meal, new DatabaseDelegate() {
            @Override
            public void onSuccess(String mealName, int Status) {
                view.showRemoveMealMessage(mealName);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    public FirebaseUser getCurrentUser() {
        return cloudRepository.getCurrentUser();
    }
}
