package com.project.foodplanner.filterresult.presenter;

import android.util.Log;

import com.project.foodplanner.filterresult.view.FilterResultViewInterface;
import com.project.foodplanner.model.RepositoryInterface;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FilterResultPresenter implements FilterResultPresenterInterface {
    private static final String TAG = "TAG filter presenter";
    private final FilterResultViewInterface view;
    private final RepositoryInterface repository;

    public FilterResultPresenter(FilterResultViewInterface view, RepositoryInterface repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void filterByIngredient(String ingredient) {
        repository.filterByIngredient(ingredient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        item -> {
                            if (item.getMeals() != null)
                                view.updateAdapterList(item.getMeals());
                            else {
                                Log.i(TAG, "filterByIngredient: no meals with " + ingredient);
                                view.updateAdapterList(new ArrayList<>());
                            }
                        },
                        error -> Log.i(TAG, "filterByIngredient: error" + error.getMessage())
                );
    }

    @Override
    public void filterByCategory(String category) {
        repository.filterByCategory(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        item -> view.updateAdapterList(item.getMeals())
                );
    }

    @Override
    public void filterByCountry(String country) {
        repository.filterByCountry(country)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        item -> view.updateAdapterList(item.getMeals())
                );
    }
}