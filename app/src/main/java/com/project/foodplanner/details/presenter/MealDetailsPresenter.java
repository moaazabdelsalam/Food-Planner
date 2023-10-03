package com.project.foodplanner.details.presenter;

import com.project.foodplanner.details.view.MealDetailsViewInterface;
import com.project.foodplanner.model.RepositoryInterface;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsPresenter implements MealDetailsPresenterInterface {
    MealDetailsViewInterface view;
    RepositoryInterface repository;

    public MealDetailsPresenter(MealDetailsViewInterface view, RepositoryInterface repository) {
        this.view = view;
        this.repository = repository;
    }


    @Override
    public void getMealById(String id) {
        repository.getMealById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        item -> view.showMeal(item.getMeals().get(0))
                );
    }
}
