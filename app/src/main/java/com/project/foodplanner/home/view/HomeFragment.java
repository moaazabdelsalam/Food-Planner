package com.project.foodplanner.home.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.project.foodplanner.R;
import com.project.foodplanner.home.presenter.HomePresenter;
import com.project.foodplanner.home.presenter.HomePresenterInterface;
import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.Repository;
import com.project.foodplanner.network.MealClient;

public class HomeFragment extends Fragment implements HomeViewInterface {
    HomePresenterInterface presenter;
    ShimmerFrameLayout todayMealShimmerLayout;
    CardView todayMealView;
    ImageView todayMealImgView;
    ImageView addFavoriteIcon;
    TextView todayMealNameTxt;
    Button addToPlanBtn;

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

        initializeViews(view);

        presenter = new HomePresenter(this, Repository.getInstance(MealClient.getInstance()));
        presenter.getRandomMeal();
    }

    @Override
    public void showTodayMeal(Meal meal) {
        todayMealShimmerLayout.stopShimmerAnimation();
        todayMealShimmerLayout.setVisibility(View.GONE);
        todayMealView.setVisibility(View.VISIBLE);

        Glide.with(getContext()).load(meal.getStrMealThumb()).into(todayMealImgView);
        todayMealNameTxt.setText(meal.getStrMeal());
        addFavoriteIcon.setImageResource(meal.isFavorite() ? R.drawable.ic_baseline_favorite_24 : R.drawable.ic_baseline_favorite_border_24);
    }

    private void initializeViews(View view) {
        todayMealShimmerLayout = view.findViewById(R.id.todayMealShimmerLayout);
        todayMealShimmerLayout.startShimmerAnimation();

        todayMealView = view.findViewById(R.id.todayMealView);
        todayMealImgView = view.findViewById(R.id.todayMealImgView);
        addFavoriteIcon = view.findViewById(R.id.addFavoriteIcon);
        todayMealNameTxt = view.findViewById(R.id.todayMealNameTxt);
        addToPlanBtn = view.findViewById(R.id.addToPlanBtn);
    }
}