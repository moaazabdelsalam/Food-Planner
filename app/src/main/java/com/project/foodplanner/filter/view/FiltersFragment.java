package com.project.foodplanner.filter.view;

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
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.reflect.TypeToken;
import com.project.foodplanner.R;
import com.project.foodplanner.database.ConcreteLocalSource;
import com.project.foodplanner.filter.presenter.FilterPresenter;
import com.project.foodplanner.filter.presenter.FilterPresenterInterface;
import com.project.foodplanner.model.Category;
import com.project.foodplanner.model.Country;
import com.project.foodplanner.model.Ingredient;
import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.Repository;
import com.project.foodplanner.model.RequestCode;
import com.project.foodplanner.network.MealClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class FiltersFragment extends Fragment implements FilterViewInterface, FilterClickListener {

    public static final String TAG = "TAG filter screen";
    SearchView searchView;
    ChipGroup chipGroup;
    Chip categoryChip;
    Chip countryChip;
    Chip ingredientChip;
    Chip mealChip;
    FilterPresenterInterface presenter;
    RecyclerView recyclerView;
    MealAdapter mealAdapter;
    CategoryAdapter categoryAdapter;
    CountryAdapter countryAdapter;
    IngredientAdapter ingredientAdapter;
    RequestCode requestCode = RequestCode.MEAL_BY_CHAR;
    FiltersFragmentDirections.ActionFiltersFragmentToFilterResultFragment action;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filters, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, int checkedId) {
                Chip chip = group.findViewById(checkedId);
                switch (chip == null ? "" : chip.getText().toString()) {
                    case "Category":
                        searchView.setQuery("", false);
                        searchView.setQueryHint("search Categories");
                        recyclerView.setVisibility(View.INVISIBLE);
                        presenter.getAllCategories();
                        requestCode = RequestCode.CATEGORIES_REQ;
                        break;
                    case "Ingredient":
                        searchView.setQuery("", false);
                        searchView.setQueryHint("search Ingredients");
                        recyclerView.setVisibility(View.INVISIBLE);
                        presenter.getAllIngredients();
                        requestCode = RequestCode.INGREDIENTS_REQ;
                        break;
                    case "Country":
                        searchView.setQuery("", false);
                        searchView.setQueryHint("search Countries");
                        recyclerView.setVisibility(View.INVISIBLE);
                        presenter.getAllCountries();
                        requestCode = RequestCode.COUNTRIES_REQ;
                        break;
                    case "Meal":
                        searchView.setQuery("", false);
                        searchView.setQueryHint("search for a meal");
                        recyclerView.setVisibility(View.INVISIBLE);
                        requestCode = RequestCode.MEAL_BY_CHAR;
                        break;
                }
            }
        });

        PublishSubject<String> newString = PublishSubject.create();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.equals(""))
                    newString.onNext(newText);
                else
                    resetRecycler();
                return false;
            }
        });

        newString.debounce(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        item -> {
                            Log.i(TAG, "on thread: " + Thread.currentThread().getName() + " onNext: " + item);
                            switch (requestCode) {
                                case MEAL_BY_CHAR:
                                    presenter.filterMeals(item);
                                    break;
                                case CATEGORIES_REQ:
                                    presenter.filterCategories(item);
                                    break;
                                case COUNTRIES_REQ:
                                    presenter.filterCountries(item);
                                    break;
                                case INGREDIENTS_REQ:
                                    presenter.filterIngredients(item);
                                    break;
                            }
                        },
                        error -> Log.i(TAG, "onError: " + error.getMessage())
                );

        /*ViewGroup.LayoutParams layoutParams1 = categoryChip.getLayoutParams();
        layoutParams1.width = ViewGroup.LayoutParams.MATCH_PARENT;
        categoryChip.setLayoutParams(layoutParams1);

        ViewGroup.LayoutParams layoutParams2 = countryChip.getLayoutParams();
        layoutParams2.width = ViewGroup.LayoutParams.MATCH_PARENT;
        countryChip.setLayoutParams(layoutParams2);*/
    }


    private void initializeViews(View view) {
        searchView = view.findViewById(R.id.searchView);
        chipGroup = view.findViewById(R.id.chipGroup);
        categoryChip = view.findViewById(R.id.categoryChip);
        countryChip = view.findViewById(R.id.countryChip);
        ingredientChip = view.findViewById(R.id.ingredientChip);
        mealChip = view.findViewById(R.id.mealChip);

        recyclerView = view.findViewById(R.id.filtersRV);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        mealAdapter = MealAdapter.getInstance(getContext(), new ArrayList<>(), this);
        ingredientAdapter = IngredientAdapter.getInstance(getContext(), new ArrayList<>(), this);
        countryAdapter = CountryAdapter.getInstance(getContext(), new ArrayList<>(), this);
        categoryAdapter = CategoryAdapter.getInstance(getContext(), new ArrayList<>(), this);

        recyclerView.setAdapter(mealAdapter);

        presenter = new FilterPresenter(
                this,
                Repository.getInstance(
                        MealClient.getInstance(),
                        ConcreteLocalSource.getInstance(getContext())
                )
        );

        action = FiltersFragmentDirections.actionFiltersFragmentToFilterResultFragment();
    }

    @Override
    public void showCategoryList(List<Category> categoryList) {
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(categoryAdapter);
        categoryAdapter.updateCategoryList(categoryList);
    }

    @Override
    public void showIngredientList(List<Ingredient> ingredientList) {
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(ingredientAdapter);
        ingredientAdapter.updateIngredientList(ingredientList);
    }

    @Override
    public void showCountryList(List<Country> countryList) {
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(countryAdapter);
        countryAdapter.updateCategoryList(countryList);
    }

    @Override
    public void showMealList(List<Meal> mealList) {
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(mealAdapter);
        mealAdapter.updateMealList(mealList);
    }

    public void resetRecycler() {
        switch (requestCode) {
            case CATEGORIES_REQ:
                presenter.getAllCategories();
                break;
            case INGREDIENTS_REQ:
                presenter.getAllIngredients();
                break;
            case COUNTRIES_REQ:
                presenter.getAllCountries();
                break;
            case MEAL_BY_CHAR:
                mealAdapter.clearMealList();
                break;
        }
    }

    @Override
    public void categoryClicked(Category category) {
        Log.i(TAG, "getting meal with category: " + category.getStrCategory());
        action.setCategory(category);
        Navigation.findNavController(recyclerView).navigate(action);
    }

    @Override
    public void countryClicked(Country country) {
        Log.i(TAG, "getting meal with country: " + country.getStrArea());
        action.setCountry(country);
        Navigation.findNavController(recyclerView).navigate(action);
    }

    @Override
    public void ingredientClicked(Ingredient ingredient) {
        Log.i(TAG, "getting meal with ingredient: " + ingredient.getStrIngredient());
        action.setIngredient(ingredient);
        Navigation.findNavController(recyclerView).navigate(action);
    }

    @Override
    public void mealClicked(Meal meal) {
        Log.i(TAG, "mealClicked: " + meal.getStrMeal());
    }
}
