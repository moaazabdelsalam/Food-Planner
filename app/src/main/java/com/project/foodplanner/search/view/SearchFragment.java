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
import android.widget.ImageView;
import android.widget.TextView;

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

public class SearchFragment extends Fragment implements SearchViewInterface, SearchClickListener {

    private static final String TAG = "TAG search fragment";
    RecyclerView categoryRecyclerView;
    TextView showAllCategories;
    RecyclerView ingredientRecyclerView;
    TextView showAllIngredients;
    CategoriesAdapter categoriesAdapter;
    IngredientsAdapter ingredientsAdapter;
    SearchPresenter presenter;
    ShimmerFrameLayout ingredientShimmerFrameLayout;
    ShimmerFrameLayout categoryShimmerFrameLayout;
    ImageView searchIcon;
    View _view;

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

        _view = view;

        initializeViews(view);

        //Log.i(TAG, "onViewCreated: " + "calling category list...");
        presenter.getAllCategories();
        presenter.getAllIngredients();

        searchIcon.setOnClickListener(view1 -> {
            openFilterScreen(view);
        });

        showAllIngredients.setOnClickListener(view1 -> onIngredientShowAllClicked());
        showAllCategories.setOnClickListener(view1 -> onCategoryShowAllClicked());
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
        ingredientShimmerFrameLayout = view.findViewById(R.id.ingredientShimmerLayout);
        ingredientShimmerFrameLayout.startShimmerAnimation();

        categoryShimmerFrameLayout = view.findViewById(R.id.categoryShimmerLayout);
        categoryShimmerFrameLayout.startShimmerAnimation();

        searchIcon = view.findViewById(R.id.searchIcon);
        categoryRecyclerView = view.findViewById(R.id.allCategoriesRV);
        ingredientRecyclerView = view.findViewById(R.id.allIngredientsRV);
        showAllCategories = view.findViewById(R.id.showAllCategories);
        showAllIngredients = view.findViewById(R.id.showAllIngredients);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);

        LinearLayoutManager linearLayoutManager0 = new LinearLayoutManager(getContext());
        linearLayoutManager0.setOrientation(RecyclerView.HORIZONTAL);

        categoriesAdapter = new CategoriesAdapter(getContext(), new ArrayList<>(), this);
        categoryRecyclerView.setLayoutManager(linearLayoutManager);
        categoryRecyclerView.setAdapter(categoriesAdapter);

        ingredientsAdapter = new IngredientsAdapter(getActivity(), new ArrayList<>(), this);
        ingredientRecyclerView.setLayoutManager(linearLayoutManager0);
        ingredientRecyclerView.setAdapter(ingredientsAdapter);


        presenter = new SearchPresenter(
                this,
                Repository.getInstance(
                        MealClient.getInstance(),
                        ConcreteLocalSource.getInstance(getContext())
                )
        );
    }

    @Override
    public void onCategoryClicked(String category) {
        SearchFragmentDirections.ActionSearchFragmentToFilterResultFragment action = SearchFragmentDirections.actionSearchFragmentToFilterResultFragment();
        action.setCategory(category);
        Navigation.findNavController(_view).navigate(action);
    }

    @Override
    public void onIngredientClicked(String ingredient) {
        SearchFragmentDirections.ActionSearchFragmentToFilterResultFragment action = SearchFragmentDirections.actionSearchFragmentToFilterResultFragment();
        action.setIngredient(ingredient);
        Navigation.findNavController(_view).navigate(action);
    }

    @Override
    public void onCategoryShowAllClicked() {
        SearchFragmentDirections.ActionSearchFragmentToFiltersFragment action = SearchFragmentDirections.actionSearchFragmentToFiltersFragment();
        action.setCategory("Category");
        Navigation.findNavController(_view).navigate(action);
    }

    @Override
    public void onIngredientShowAllClicked() {
        SearchFragmentDirections.ActionSearchFragmentToFiltersFragment action = SearchFragmentDirections.actionSearchFragmentToFiltersFragment();
        action.setIngredient("Ingredient");
        Navigation.findNavController(_view).navigate(action);
    }
}