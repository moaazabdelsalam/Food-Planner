package com.project.foodplanner.search.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.project.foodplanner.R;
import com.project.foodplanner.database.ConcreteLocalSource;
import com.project.foodplanner.model.Category;
import com.project.foodplanner.model.Ingredient;
import com.project.foodplanner.model.Repository;
import com.project.foodplanner.network.MealClient;
import com.project.foodplanner.search.presenter.SearchPresenter;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements SearchViewInterface {

    private static final String TAG = "TAG search fragment";
    RecyclerView categoryRecyclerView;
    RecyclerView ingredientRecyclerView;
    CategoriesAdapter categoriesAdapter;
    IngredientsAdapter ingredientsAdapter;
    SearchPresenter presenter;
    ShimmerFrameLayout ingredientShimmerFrameLayout;
    ShimmerFrameLayout categoryShimmerFrameLayout;
    SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);

        //Log.i(TAG, "onViewCreated: " + "calling category list...");
        presenter.getAllCategories();
        presenter.getAllIngredients();

        searchView.setOnClickListener(view1 -> {
            openFilterScreen(view);
        });
    }

    @Override
    public void showCategoryList(List<Category> categoryList) {
        categoriesAdapter.updateCategoryList(categoryList);
        categoryShimmerFrameLayout.stopShimmerAnimation();
        categoryShimmerFrameLayout.setVisibility(View.GONE);
        categoryRecyclerView.setVisibility(View.VISIBLE);
        //Log.i(TAG, "showCategoryList: " + categoryList);
    }

    @Override
    public void showIngredientList(List<Ingredient> ingredientList) {
        ingredientsAdapter.updateIngredientList(ingredientList);
        //Log.i(TAG, "showIngredientList: " + ingredientList);
        ingredientShimmerFrameLayout.stopShimmerAnimation();
        ingredientShimmerFrameLayout.setVisibility(View.GONE);
        ingredientRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void openFilterScreen(View view) {
        Navigation.findNavController(view).navigate(R.id.action_searchFragment_to_filtersFragment);
    }

    public void initializeViews(@NonNull View view) {
        searchView = view.findViewById(R.id.searchView);
        categoryRecyclerView = view.findViewById(R.id.allCategoriesRV);
        ingredientRecyclerView = view.findViewById(R.id.allIngredientsRV);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);

        LinearLayoutManager linearLayoutManager0 = new LinearLayoutManager(getContext());
        linearLayoutManager0.setOrientation(RecyclerView.HORIZONTAL);

        categoriesAdapter = new CategoriesAdapter(getContext(), new ArrayList<>());
        categoryRecyclerView.setLayoutManager(linearLayoutManager);
        categoryRecyclerView.setAdapter(categoriesAdapter);

        ingredientsAdapter = new IngredientsAdapter(getActivity(), new ArrayList<>());
        ingredientRecyclerView.setLayoutManager(linearLayoutManager0);
        ingredientRecyclerView.setAdapter(ingredientsAdapter);

        ingredientShimmerFrameLayout = view.findViewById(R.id.ingredientShimmerLayout);
        ingredientShimmerFrameLayout.startShimmerAnimation();

        categoryShimmerFrameLayout = view.findViewById(R.id.categoryShimmerLayout);
        categoryShimmerFrameLayout.startShimmerAnimation();

        presenter = new SearchPresenter(
                this,
                Repository.getInstance(
                        MealClient.getInstance(),
                        ConcreteLocalSource.getInstance(getContext())
                )
        );
    }
}