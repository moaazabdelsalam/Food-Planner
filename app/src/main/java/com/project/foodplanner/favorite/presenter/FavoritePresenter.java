package com.project.foodplanner.favorite.presenter;

import android.util.Log;

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

        if (cloudRepository.getCurrentUser() == null) {
            view.showNotLoggedInMessage();
        } else {
            getAllFavMeals();
        }
    }

    @Override
    public void getAllFavMeals() {
        repository.getFavDBContent(cloudRepository.getCurrentUser().getUid())
                .subscribe(
                        mealList -> {
                            Log.i(TAG, "getAllFavMeals: got list of " + mealList.size());
                            if (!mealList.isEmpty()) {
                                view.hidePlaceholders();
                                view.showAllFavoriteMeals(mealList);
                            } else
                                view.showPlaceholders();
                            //mealList.forEach(meal -> view.showFavoriteMeal(meal));
                        },
                        error -> Log.i(TAG, "getAllFavMeals: " + error.getMessage())
                );
    }

    @Override
    public void removeMeal(Meal meal) {
        repository.removeMealFromFavDB(meal, new DatabaseDelegate() {
            @Override
            public void onSuccess(String mealName, int Status) {
                view.showRemoveMealMessage(mealName);
                getAllFavMeals();
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
