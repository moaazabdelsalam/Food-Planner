package com.project.foodplanner.model;

public enum RequestCode {
    CATEGORIES_REQ("Category"),
    INGREDIENTS_REQ("Ingredient"),
    RANDOM_MEAL_REQ("Meal random"),
    COUNTRIES_REQ("Country"),
    MEAL_BY_CHAR("Meal by char");

    private final String code;

    RequestCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
