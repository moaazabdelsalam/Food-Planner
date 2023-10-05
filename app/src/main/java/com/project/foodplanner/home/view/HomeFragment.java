package com.project.foodplanner.home.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
import com.project.foodplanner.R;
import com.project.foodplanner.database.ConcreteLocalSource;
import com.project.foodplanner.home.presenter.HomePresenter;
import com.project.foodplanner.home.presenter.HomePresenterInterface;
import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.Repository;
import com.project.foodplanner.network.MealClient;

public class HomeFragment extends Fragment implements HomeViewInterface {
    private static final String TAG = "TAG home fragment";
    HomePresenterInterface presenter;
    ShimmerFrameLayout todayMealShimmerLayout;
    CardView todayMealView;
    ImageView todayMealImgView;
    ImageView addFavoriteIcon;
    TextView todayMealNameTxt;
    Button addToPlanBtn;
    View _view;

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

        _view = getView();

        initializeViews(view);
        presenter.getTodayMeal();
        addFavoriteIcon.setOnClickListener(view1 -> presenter.todayMealFavoriteClick());
        todayMealImgView.setOnClickListener(view1 -> presenter.sendMealID());
        addToPlanBtn.setOnClickListener(view1 -> {
            Log.i(TAG, "onViewCreated: add to plan clicked");
            presenter.addTodayMealToPlan("2");
        });
    }

    @Override
    public void showTodayMeal(Meal meal) {
        todayMealShimmerLayout.stopShimmerAnimation();
        todayMealShimmerLayout.setVisibility(View.GONE);
        todayMealView.setVisibility(View.VISIBLE);

        Glide.with(getContext()).load(meal.getStrMealThumb()).placeholder(R.drawable.image_placeholder).into(todayMealImgView);
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
                    }).show();
        } else if (status == 0) {
            addFavoriteIcon.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            Snackbar.make(_view, meal + " removed from favorite", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(_view, "something error happened", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void gotToMealDetails(String mealID) {
        HomeFragmentDirections.ActionHomeFragmentToMealDetailsFragment action = HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(mealID);
        Navigation.findNavController(_view).navigate(action);
    }

    private void initializeViews(View view) {
        todayMealShimmerLayout = view.findViewById(R.id.todayMealShimmerLayout);
        todayMealShimmerLayout.startShimmerAnimation();

        todayMealView = view.findViewById(R.id.todayMealView);
        todayMealImgView = view.findViewById(R.id.todayMealImgView);
        addFavoriteIcon = view.findViewById(R.id.addFavoriteIcon);
        todayMealNameTxt = view.findViewById(R.id.todayMealNameTxt);
        addToPlanBtn = view.findViewById(R.id.homeAddToPlanBtn);
        presenter = new HomePresenter(this, Repository.getInstance(MealClient.getInstance(), ConcreteLocalSource.getInstance(getContext())));
    }
}