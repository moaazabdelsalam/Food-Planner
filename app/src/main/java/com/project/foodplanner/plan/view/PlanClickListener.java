package com.project.foodplanner.plan.view;

public interface PlanClickListener {
    void onMealImgClick(String mealId);

    void onCategoryTxtClicked(String category);

    void onCountryTxtClicked(String country);

    void onRemovePlanClicked(String mealId);
}
