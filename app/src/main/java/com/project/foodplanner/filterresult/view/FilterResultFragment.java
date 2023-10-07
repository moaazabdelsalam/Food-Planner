package com.project.foodplanner.filterresult.view;

import android.icu.util.Calendar;
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
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.project.foodplanner.R;
import com.project.foodplanner.database.ConcreteLocalSource;
import com.project.foodplanner.filterresult.presenter.FilterResultPresenter;
import com.project.foodplanner.filterresult.presenter.FilterResultPresenterInterface;
import com.project.foodplanner.model.DayPickerDialog;
import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.Repository;
import com.project.foodplanner.network.DatabaseDelegate;
import com.project.foodplanner.network.MealClient;

import java.util.ArrayList;
import java.util.List;

public class FilterResultFragment extends Fragment implements FilterResultViewInterface, FilterResultClickListener {
    private static final String TAG = "TAG filter result fragment";
    TextView resultFilterTxt;
    RecyclerView recyclerView;
    FilterResultAdapter filterResultAdapter;
    FilterResultPresenterInterface presenter;
    LottieAnimationView resultAnimation;
    View _view;
    Calendar calendar = Calendar.getInstance();

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
        _view = getView();
        initializeViews(view);

        String ingredient = FilterResultFragmentArgs.fromBundle(getArguments()).getIngredient();
        String category = FilterResultFragmentArgs.fromBundle(getArguments()).getCategory();
        String country = FilterResultFragmentArgs.fromBundle(getArguments()).getCountry();

        if (ingredient != null) {
            Log.i("TAG", "got ingredient: ");
            presenter.filterByIngredient(ingredient);
            resultFilterTxt.setText("all Meals by " + ingredient);
        } else if (category != null) {
            Log.i("TAG", "got category");
            presenter.filterByCategory(category);
            resultFilterTxt.setText("all Meals by " + category);
        } else if (country != null) {
            Log.i("TAG", "got country");
            presenter.filterByCountry(country);
            resultFilterTxt.setText("all Meals from " + country);
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
    public void addToFavorite(Meal meal) {
        presenter.addToFavorite(meal);
    }

    @Override
    public void removeFromFavorite(Meal meal) {
        presenter.removeFromFavorite(meal);
    }

    @Override
    public void pickDateAndAddMealToPlan(Meal meal) {
        MaterialDatePicker<Long> dayPickerDialog = DayPickerDialog.showDialog(requireActivity().getSupportFragmentManager());
        dayPickerDialog.addOnPositiveButtonClickListener(selection -> {

            calendar.setTimeInMillis(selection);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            Log.i(TAG, "onViewCreated: selected day: " + day);
            presenter.addMealToPlan(meal, String.valueOf(day));
        });
    }

    @Override
    public void showFavoriteClickMessage(String mealName, int status) {
        if (status == 1) {
            Snackbar.make(_view, mealName + " added to favorite", Snackbar.LENGTH_SHORT)
                    .setAction("View", view -> {
                        Navigation.findNavController(_view).navigate(R.id.action_filterResultFragment_to_favoriteFragment);
                    }).show();
        } else if (status == 0) {
            Snackbar.make(_view, mealName + " removed from favorite", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(_view, "something error happened", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showAddToPlanMessage(String meal, int status) {
        if (status == 1) {
            Snackbar.make(_view, meal + " added to plan", Snackbar.LENGTH_SHORT)
                    .setAction("View", view -> {
                        Navigation.findNavController(_view).navigate(R.id.action_filterResultFragment_to_planFragment);
                    }).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("TAG", "onDestroy: ");
        presenter.clearCache();
    }
}