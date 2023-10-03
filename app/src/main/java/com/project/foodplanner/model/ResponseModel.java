package com.project.foodplanner.model;

import java.util.List;

public class ResponseModel {
    private List<Object> meals;

    public ResponseModel(List<Object> meals) {
        this.meals = meals;
    }

    public List<Object> getMeals() {
        return meals;
    }

    public void setMeals(List<Object> meals) {
        this.meals = meals;
    }
}
