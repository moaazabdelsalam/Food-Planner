package com.project.foodplanner.filterresult.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.project.foodplanner.R;
import com.project.foodplanner.database.ConcreteLocalSource;
import com.project.foodplanner.filterresult.presenter.FilterResultPresenter;
import com.project.foodplanner.filterresult.presenter.FilterResultPresenterInterface;
import com.project.foodplanner.model.Category;
import com.project.foodplanner.model.Country;
import com.project.foodplanner.model.Ingredient;
import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.Repository;
import com.project.foodplanner.network.MealClient;

import java.util.ArrayList;
import java.util.List;

public class FilterResultFragment extends Fragment implements FilterResultViewInterface, FilterResultClickListener {
    TextView resultFilterTxt;
    RecyclerView recyclerView;
    FilterResultAdapter filterResultAdapter;
    FilterResultPresenterInterface presenter;
    LottieAnimationView resultAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i("TAG", "onViewCreated: ");

        initializeViews(view);

        Ingredient ingredient = FilterResultFragmentArgs.fromBundle(getArguments()).getIngredient();
        Category category = FilterResultFragmentArgs.fromBundle(getArguments()).getCategory();
        Country country = FilterResultFragmentArgs.fromBundle(getArguments()).getCountry();

        if (ingredient != null) {
            Log.i("TAG", "got ingredient: ");
            presenter.filterByIngredient(ingredient.getStrIngredient());
            resultFilterTxt.setText("all Meals by " + ingredient.getStrIngredient());
        } else if (category != null) {
            Log.i("TAG", "got category");
            presenter.filterByCategory(category.getStrCategory());
            resultFilterTxt.setText("all Meals by " + category.getStrCategory());
        } else if (country != null) {
            Log.i("TAG", "got country");
            presenter.filterByCountry(country.getStrArea());
            resultFilterTxt.setText("all Meals from " + country.getStrArea());
        }
    }

    private void initializeViews(View view) {
        resultAnimation = view.findViewById(R.id.resultAnimation);

        resultFilterTxt = view.findViewById(R.id.resultFilterTxt);
        recyclerView = view.findViewById(R.id.filterResultRV);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        filterResultAdapter = new FilterResultAdapter(getContext(), new ArrayList<>(), this);
        recyclerView.setAdapter(filterResultAdapter);

        presenter = new FilterResultPresenter(
                this,
                Repository.getInstance(
                        MealClient.getInstance(),
                        ConcreteLocalSource.getInstance(getContext())
                ));

    }

    @Override
    public void updateAdapterList(List<Meal> mealList) {
        resultAnimation.pauseAnimation();
        resultAnimation.setVisibility(View.INVISIBLE);
        if (mealList.isEmpty()) {
            Toast.makeText(getContext(), "no meals found", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(recyclerView).navigateUp();
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            filterResultAdapter.updateMealList(mealList);
        }
    }

    @Override
    public void showMealDetails(Meal meal) {
        FilterResultFragmentDirections.ActionFilterResultFragmentToMealDetailsFragment action = FilterResultFragmentDirections.actionFilterResultFragmentToMealDetailsFragment(meal.getIdMeal());
        Navigation.findNavController(recyclerView).navigate(action);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("TAG", "onDestroy: ");
        presenter.clearCache();
    }
}