package com.project.foodplanner.details.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.project.foodplanner.R;
import com.project.foodplanner.database.ConcreteLocalSource;
import com.project.foodplanner.details.presenter.MealDetailsPresenter;
import com.project.foodplanner.details.presenter.MealDetailsPresenterInterface;
import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.Repository;
import com.project.foodplanner.network.MealClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.core.Observable;

public class MealDetailsFragment extends Fragment implements MealDetailsViewInterface {
    ImageView mealImg;
    ImageView addToFav;
    TextView mealName;
    TextView mealTags;
    TextView mealInstructionTxt;
    TextView mealIngredientTxt;
    AppCompatButton addToPlanBtn;
    YouTubePlayerView youTubePlayerView;
    ShimmerFrameLayout detailsShimmer;
    TextView instructions;
    TextView ingredients;
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
        detailsShimmer.startShimmerAnimation();

        String mealID = MealDetailsFragmentArgs.fromBundle(getArguments()).getMealID();
        presenter.getMealById(mealID);
    }

    private void initializeViews(View view) {
        mealImg = view.findViewById(R.id.mealImg);
        addToFav = view.findViewById(R.id.addToFav);
        mealName = view.findViewById(R.id.mealName);
        mealTags = view.findViewById(R.id.mealTags);
        mealInstructionTxt = view.findViewById(R.id.mealInstructionTxt);
        mealIngredientTxt = view.findViewById(R.id.mealIngredientTxt);
        instructions = view.findViewById(R.id.instruction);
        ingredients = view.findViewById(R.id.ingredients);
        addToPlanBtn = view.findViewById(R.id.addToPlanBtn);

        youTubePlayerView = view.findViewById(R.id.videoPlayer);
        getLifecycle().addObserver(youTubePlayerView);

        detailsShimmer = view.findViewById(R.id.detailsShimmerLayout);

        presenter = new MealDetailsPresenter(this,
                Repository.getInstance(
                        MealClient.getInstance(),
                        ConcreteLocalSource.getInstance(getContext())
                )
        );
    }

    @Override
    public void showMeal(Meal meal) {
        detailsShimmer.stopShimmerAnimation();
        detailsShimmer.setVisibility(View.GONE);
        mealImg.setVisibility(View.VISIBLE);
        addToFav.setVisibility(View.VISIBLE);
        youTubePlayerView.setVisibility(View.VISIBLE);
        ingredients.setVisibility(View.VISIBLE);
        mealIngredientTxt.setVisibility(View.VISIBLE);
        instructions.setVisibility(View.VISIBLE);
        mealInstructionTxt.setVisibility(View.VISIBLE);
        addToPlanBtn.setVisibility(View.VISIBLE);

        Glide.with(requireContext()).load(meal.getStrMealThumb()).placeholder(R.drawable.image_placeholder).into(mealImg);
        mealName.setText(meal.getStrMeal());
        if (meal.getStrArea() != null)
            mealTags.append(meal.getStrArea());
        if (meal.getStrCategory() != null)
            mealTags.append(", " + meal.getStrCategory());
        if (meal.getStrTags() != null)
            mealTags.append(", " + meal.getStrTags());
        mealInstructionTxt.setText(meal.getStrInstructions());

        String videoId = "";
        int index = meal.getStrYoutube().indexOf("v=");
        if (index != -1) {
            videoId = meal.getStrYoutube().substring(index + 2);
        }
        String finalVideoId = videoId;
        youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> {
            YouTubePlayerUtils.loadOrCueVideo(youTubePlayer, getLifecycle(), finalVideoId, 0.0f);
        });

        Observable.fromIterable(pairIngredients(meal)).subscribe(
                item -> {
                    if (!(item.first.isEmpty() || item.second.isEmpty())) {
                        Log.i("TAG", "showMealIngredients: " + item.first + ": " + item.second);
                        mealIngredientTxt.append(item.first + ": " + item.second + "\n");
                    }
                },
                error -> Log.i("TAG", "showMeal: error" + error.getMessage())
        );
    }

    private List<Pair<String, String>> pairIngredients(Meal meal) {
        List<Pair<String, String>> ingredientList = new ArrayList<>();

        ingredientList.add(new Pair<>(meal.getStrIngredient1(), meal.getStrMeasure1()));
        ingredientList.add(new Pair<>(meal.getStrIngredient2(), meal.getStrMeasure2()));
        ingredientList.add(new Pair<>(meal.getStrIngredient3(), meal.getStrMeasure3()));
        ingredientList.add(new Pair<>(meal.getStrIngredient4(), meal.getStrMeasure4()));
        ingredientList.add(new Pair<>(meal.getStrIngredient5(), meal.getStrMeasure5()));
        ingredientList.add(new Pair<>(meal.getStrIngredient6(), meal.getStrMeasure6()));
        ingredientList.add(new Pair<>(meal.getStrIngredient7(), meal.getStrMeasure7()));
        ingredientList.add(new Pair<>(meal.getStrIngredient8(), meal.getStrMeasure8()));
        ingredientList.add(new Pair<>(meal.getStrIngredient9(), meal.getStrMeasure9()));
        ingredientList.add(new Pair<>(meal.getStrIngredient10(), meal.getStrMeasure10()));
        ingredientList.add(new Pair<>(meal.getStrIngredient11(), meal.getStrMeasure11()));
        ingredientList.add(new Pair<>(meal.getStrIngredient12(), meal.getStrMeasure12()));
        ingredientList.add(new Pair<>(meal.getStrIngredient13(), meal.getStrMeasure13()));
        ingredientList.add(new Pair<>(meal.getStrIngredient14(), meal.getStrMeasure14()));
        ingredientList.add(new Pair<>(meal.getStrIngredient15(), meal.getStrMeasure15()));
        ingredientList.add(new Pair(meal.getStrIngredient16(), meal.getStrMeasure16()));
        ingredientList.add(new Pair(meal.getStrIngredient17(), meal.getStrMeasure17()));
        ingredientList.add(new Pair(meal.getStrIngredient18(), meal.getStrMeasure18()));
        ingredientList.add(new Pair(meal.getStrIngredient19(), meal.getStrMeasure19()));
        ingredientList.add(new Pair(meal.getStrIngredient20(), meal.getStrMeasure20()));

        return ingredientList;
    }
}