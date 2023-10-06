package com.project.foodplanner.favorite.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.project.foodplanner.R;
import com.project.foodplanner.database.ConcreteLocalSource;
import com.project.foodplanner.favorite.presenter.FavoritePresenter;
import com.project.foodplanner.favorite.presenter.FavoritePresenterInterface;
import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.Repository;
import com.project.foodplanner.network.MealClient;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment implements FavoriteViewInterface, FavoriteClickListener {
    RecyclerView recyclerView;
    FavoriteAdapter favoriteAdapter;
    FavoritePresenterInterface presenter;
    View _view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _view = view;

        initializeViews(view);
        presenter.getAllFavMeals().observe(getViewLifecycleOwner(), new Observer<List<Meal>>() {
            @Override
            public void onChanged(List<Meal> meals) {
                favoriteAdapter.updateFavMealList(meals);
            }
        });
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.favoriteRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        favoriteAdapter = new FavoriteAdapter(getContext(), new ArrayList<>(), this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(favoriteAdapter);

        presenter = new FavoritePresenter(this,
                Repository.getInstance(
                        MealClient.getInstance(),
                        ConcreteLocalSource.getInstance(getContext())
                )
        );
    }

    @Override
    public void removeMealFromFav(Meal meal) {
        presenter.removeMeal(meal);
    }

    @Override
    public void showRemoveMealMessage(String mealName) {
        Toast.makeText(getContext(), mealName + " removed from favorite", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFavIconClicked(Meal meal) {
        removeMealFromFav(meal);
    }

    @Override
    public void onCategoryTxtClicked(String category) {
        FavoriteFragmentDirections.ActionFavoriteFragmentToFilterResultFragment action = FavoriteFragmentDirections.actionFavoriteFragmentToFilterResultFragment();
        action.setCategory(category);
        Navigation.findNavController(_view).navigate(action);
    }

    @Override
    public void onCountryTxtClicked(String country) {
        FavoriteFragmentDirections.ActionFavoriteFragmentToFilterResultFragment action = FavoriteFragmentDirections.actionFavoriteFragmentToFilterResultFragment();
        action.setCountry(country);
        Navigation.findNavController(_view).navigate(action);
    }

    @Override
    public void onImgClicked(String mealId) {
        FavoriteFragmentDirections.ActionFavoriteFragmentToMealDetailsFragment action = FavoriteFragmentDirections.actionFavoriteFragmentToMealDetailsFragment(mealId);
        Navigation.findNavController(_view).navigate(action);
    }
}