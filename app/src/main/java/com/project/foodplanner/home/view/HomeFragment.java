package com.project.foodplanner.home.view;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.project.foodplanner.LoginActivity;
import com.project.foodplanner.filterresult.view.FilterResultAdapter;
import com.project.foodplanner.filterresult.view.FilterResultClickListener;
import com.project.foodplanner.model.CloudRepo;
import com.project.foodplanner.model.DayPickerDialog;
import com.project.foodplanner.R;
import com.project.foodplanner.database.ConcreteLocalSource;
import com.project.foodplanner.home.presenter.HomePresenter;
import com.project.foodplanner.home.presenter.HomePresenterInterface;
import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.MealsRepository;
import com.project.foodplanner.model.NetworkUtils;
import com.project.foodplanner.model.SimpleMeal;
import com.project.foodplanner.network.MealClient;
import com.project.foodplanner.plan.view.PlanClickListener;
import com.project.foodplanner.plan.view.PlanRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeViewInterface, FilterResultClickListener, PlanClickListener {
    private static final String TAG = "TAG home fragment";
    HomePresenterInterface presenter;
    ShimmerFrameLayout todayMealShimmerLayout;
    ShimmerFrameLayout regionShimmerLayout;
    RecyclerView popularInRegionRV;
    RecyclerView todayPlanRV;
    FilterResultAdapter regionAdapter;
    PlanRecyclerViewAdapter planRecyclerViewAdapter;
    CardView todayMealView;
    ImageView todayMealImgView;
    ImageView addFavoriteIcon;
    TextView todayMealNameTxt;
    Button addToPlanBtn;
    ImageView noPlansImagePlaceholder;
    TextView noPlansTxtPlaceholder;
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        _view = getView();

        initializeViews(view);
        if (!NetworkUtils.isNetworkAvailable(getContext())) {
            new MaterialAlertDialogBuilder(getContext())
                    .setTitle("No Network")
                    .setMessage("no internet connection found please check you wifi or mobile data to be able to use all features of application")
                    .setPositiveButton("Ok", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        }
        if (presenter.getCurrentUser() != null)
            presenter.getTodayPlan(String.valueOf(dayOfMonth));
        presenter.getTodayMeal();
        presenter.getMealsOfCountry("Egyptian");

        addFavoriteIcon.setOnClickListener(view1 -> presenter.todayMealFavoriteClick());
        todayMealImgView.setOnClickListener(view1 -> presenter.sendMealID());
        addToPlanBtn.setOnClickListener(view1 -> {
            Log.i(TAG, "onViewCreated: add to plan clicked");
            MaterialDatePicker<Long> dayPickerDialog = DayPickerDialog.showDialog(requireActivity().getSupportFragmentManager());
            dayPickerDialog.addOnPositiveButtonClickListener(selection -> {

                calendar.setTimeInMillis(selection);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                Log.i(TAG, "onViewCreated: selected day: " + day);
                presenter.addTodayMealToPlan(String.valueOf(day));
            });
        });
    }

    @Override
    public void showTodayMeal(Meal meal) {
        todayMealShimmerLayout.stopShimmerAnimation();
        todayMealShimmerLayout.setVisibility(View.GONE);
        todayMealView.setVisibility(View.VISIBLE);

        Glide.with(requireContext()).load(meal.getStrMealThumb()).placeholder(R.drawable.image_placeholder).into(todayMealImgView);
        todayMealNameTxt.setText(meal.getStrMeal());
        addFavoriteIcon.setImageResource(meal.isFavorite() ? R.drawable.ic_baseline_favorite_24 : R.drawable.ic_baseline_favorite_border_24);
        addFavoriteIcon.setTag(meal.isFavorite() ? R.drawable.ic_baseline_favorite_24 : R.drawable.ic_baseline_favorite_border_24);
    }

    @Override
    public void showAddFavoriteMessage(String meal, int status) {
        if (status == 1) {
            addFavoriteIcon.setImageResource(R.drawable.ic_baseline_favorite_24);
            Snackbar.make(_view, meal + " added to favorite", Snackbar.LENGTH_SHORT)
                    .setAction("View", view -> {
                        Navigation.findNavController(_view).navigate(R.id.action_homeFragment_to_favoriteFragment);
                    })
                    .setAnchorView(R.id.bottomNav)
                    .show();
        } else if (status == 0) {
            addFavoriteIcon.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            Snackbar.make(_view, meal + " removed from favorite", Snackbar.LENGTH_SHORT).setAnchorView(R.id.bottomNav).show();
        } else {
            Snackbar.make(_view, "something error happened", Snackbar.LENGTH_SHORT).setAnchorView(R.id.bottomNav).show();
        }
    }

    @Override
    public void showAddToPlanMessage(String meal, int status) {
        if (status == 1) {
            Snackbar.make(_view, meal + " added to plan", Snackbar.LENGTH_SHORT)
                    .setAction("View", view -> {
                        Navigation.findNavController(_view).navigate(R.id.action_homeFragment_to_planFragment);
                    }).setAnchorView(R.id.bottomNav).setAnchorView(R.id.bottomNav).show();
        }
    }

    @Override
    public void showTodayPlanMeal(SimpleMeal simpleMeal) {
        noPlansTxtPlaceholder.setVisibility(View.GONE);
        noPlansImagePlaceholder.setVisibility(View.GONE);
        todayPlanRV.setVisibility(View.VISIBLE);
        planRecyclerViewAdapter.addToList(simpleMeal);
    }

    @Override
    public void goToMealDetails(String mealID) {
        HomeFragmentDirections.ActionHomeFragmentToMealDetailsFragment action = HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(mealID);
        Navigation.findNavController(_view).navigate(action);
    }

    @Override
    public void showCountryMeals(List<Meal> meals) {
        regionShimmerLayout.stopShimmerAnimation();
        regionShimmerLayout.setVisibility(View.GONE);
        regionAdapter.updateMealList(meals);
    }

    @Override
    public void showNotLoggedInMessage() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Login First")
                .setMessage("Some features available only when you are logged in, Please login first")
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Respond to negative button press
                    Navigation.findNavController(_view).navigateUp();
                })
                .setPositiveButton("Ok", (dialog, which) -> {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                })
                .setOnDismissListener(dialogInterface -> Navigation.findNavController(_view).navigateUp())
                .show();

    }

    private void initializeViews(View view) {
        todayMealShimmerLayout = view.findViewById(R.id.todayMealShimmerLayout);
        todayMealShimmerLayout.startShimmerAnimation();

        regionShimmerLayout = view.findViewById(R.id.regionShimmerLayout);
        regionShimmerLayout.startShimmerAnimation();

        popularInRegionRV = view.findViewById(R.id.popularInRegionRV);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        linearLayoutManager2.setOrientation(RecyclerView.HORIZONTAL);
        regionAdapter = new FilterResultAdapter(getContext(), new ArrayList<>(), this);
        popularInRegionRV.setLayoutManager(linearLayoutManager2);
        popularInRegionRV.setAdapter(regionAdapter);

        todayPlanRV = view.findViewById(R.id.todayPlanRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        planRecyclerViewAdapter = new PlanRecyclerViewAdapter(getContext(), new ArrayList<>(), this);
        todayPlanRV.setLayoutManager(linearLayoutManager);
        todayPlanRV.setAdapter(planRecyclerViewAdapter);

        todayMealView = view.findViewById(R.id.todayMealView);
        todayMealImgView = view.findViewById(R.id.todayMealImgView);
        addFavoriteIcon = view.findViewById(R.id.addFavoriteIcon);
        todayMealNameTxt = view.findViewById(R.id.todayMealNameTxt);
        addToPlanBtn = view.findViewById(R.id.homeAddToPlanBtn);
        noPlansImagePlaceholder = view.findViewById(R.id.noPlansImgPlaceholder);
        noPlansTxtPlaceholder = view.findViewById(R.id.noPlansTxtPlaceholder);

        presenter = new HomePresenter(this,
                MealsRepository.getInstance(
                        MealClient.getInstance(),
                        ConcreteLocalSource.getInstance(getContext())
                ),
                CloudRepo.getInstance(MealsRepository.getInstance(
                                MealClient.getInstance(),
                                ConcreteLocalSource.getInstance(getContext())
                        )
                ));
    }

    @Override
    public void showMealDetails(Meal meal) {
        HomeFragmentDirections.ActionHomeFragmentToMealDetailsFragment action = HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(meal.getIdMeal());
        Navigation.findNavController(_view).navigate(action);
    }

    @Override
    public void addToFavorite(Meal meal) {
        presenter.addMealToFavorite(meal);
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
    public void onMealImgClick(String mealId) {
        HomeFragmentDirections.ActionHomeFragmentToMealDetailsFragment action = HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(mealId);
        Navigation.findNavController(_view).navigate(action);
    }

    @Override
    public void onCategoryTxtClicked(String category) {
        HomeFragmentDirections.ActionHomeFragmentToFilterResultFragment action = HomeFragmentDirections.actionHomeFragmentToFilterResultFragment();
        action.setCategory(category);
        Navigation.findNavController(_view).navigate(action);
    }

    @Override
    public void onCountryTxtClicked(String country) {
        HomeFragmentDirections.ActionHomeFragmentToFilterResultFragment action = HomeFragmentDirections.actionHomeFragmentToFilterResultFragment();
        action.setCountry(country);
        Navigation.findNavController(_view).navigate(action);
    }
}