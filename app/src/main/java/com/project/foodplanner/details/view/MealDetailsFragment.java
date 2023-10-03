package com.project.foodplanner.details.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.foodplanner.R;
import com.project.foodplanner.database.ConcreteLocalSource;
import com.project.foodplanner.details.presenter.MealDetailsPresenter;
import com.project.foodplanner.details.presenter.MealDetailsPresenterInterface;
import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.Repository;
import com.project.foodplanner.network.MealClient;

public class MealDetailsFragment extends Fragment implements MealDetailsViewInterface {
    ImageView mealImg;
    ImageView addToFav;
    TextView mealName;
    TextView mealTags;
    TextView mealInstructionTxt;
    TextView mealIngredientTxt;
    MealDetailsPresenterInterface presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
        Meal meal = MealDetailsFragmentArgs.fromBundle(getArguments()).getMeal();
        Glide.with(view).load(meal.getStrMealThumb()).placeholder(R.drawable.image_placeholder).into(mealImg);
        mealName.setText(meal.getStrMeal());
        if (meal.getStrArea() != null)
            mealTags.append(meal.getStrArea());
        if (meal.getStrCategory() != null)
            mealTags.append(", " + meal.getStrCategory());
        if (meal.getStrTags() != null)
            mealTags.append(", " + meal.getStrTags());
        mealInstructionTxt.setText(meal.getStrInstructions());
    }

    private void initializeViews(View view) {
        mealImg = view.findViewById(R.id.mealImg);
        addToFav = view.findViewById(R.id.addToFav);
        mealName = view.findViewById(R.id.mealName);
        mealTags = view.findViewById(R.id.mealTags);
        mealInstructionTxt = view.findViewById(R.id.mealInstructionTxt);
        mealIngredientTxt = view.findViewById(R.id.mealIngredientTxt);

        presenter = new MealDetailsPresenter(this,
                Repository.getInstance(
                        MealClient.getInstance(),
                        ConcreteLocalSource.getInstance(getContext())
                )
        );
    }

    @Override
    public void showMeal(Meal meal) {

    }
}