package com.project.foodplanner.network;

import com.google.gson.JsonObject;
import com.project.foodplanner.model.Category;
import com.project.foodplanner.model.RequestCode;

import java.util.List;

public interface NetworkCallback {
    void onSuccessResult(RequestCode requestCode, JsonObject jsonObject);

    void onFailureResult(String errorMsg);
}
