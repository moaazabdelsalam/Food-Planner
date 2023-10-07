package com.project.foodplanner.details.presenter;

import com.project.foodplanner.model.Meal;
import com.project.foodplanner.network.DatabaseDelegate;
import com.project.foodplanner.details.view.MealDetailsViewInterface;
import com.project.foodplanner.model.MealsRepositoryInterface;
import com.project.foodplanner.network.NetworkDelegate;
import com.project.foodplanner.utils.DummyCache;

import java.util.List;

public class MealDetailsPresenter implements MealDetailsPresenterInterface {
    MealDetailsViewInterface view;
    MealsRepositoryInterface repository;
    DummyCache cache = DummyCache.getInstance();

    public MealDetailsPresenter(MealDetailsViewInterface view, MealsRepositoryInterface repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void getMealById(String id) {
        repository.getMealById(id, new NetworkDelegate() {
            @Override
            public void onSuccess(List<Meal> mealList) {
                view.showMeal(mealList.get(mealList.size() - 1));
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    public void favoriteClick() {
        repository.detailsMealClick(new DatabaseDelegate() {
            @Override
            public void onSuccess(String mealName, int status) {
                view.showFavoriteClickMessage(mealName, status);
            }

            @Override
            public void onError(String error) {
                view.showFavoriteClickMessage("NAN", -1);
            }
        });
    }

    @Override
    public void addDetailsMealToPlan(String dayId) {
        repository.insertDetailsMealToPlan(dayId, new DatabaseDelegate() {
            @Override
            public void onSuccess(String mealName, int status) {
                view.showAddToPlanMessage(mealName, status);
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
