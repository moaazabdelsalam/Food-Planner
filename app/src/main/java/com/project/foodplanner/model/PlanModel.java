package com.project.foodplanner.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "plan_table",
        foreignKeys = {@ForeignKey(entity = SimpleMeal.class, parentColumns = "idMeal", childColumns = "idMeal", onDelete = ForeignKey.CASCADE)},
        primaryKeys = {"dayID", "idMeal"})
public class PlanModel {

    long id;
    @NonNull
    String dayID;
    @NonNull
    private String idMeal;
    private String userID;

    public PlanModel(long id, String dayID, String idMeal, String userID) {
        this.id = id;
        this.dayID = dayID;
        this.idMeal = idMeal;
        this.userID = userID;
    }

    @Ignore
    public PlanModel(String dayID, String idMeal) {
        this.dayID = dayID;
        this.idMeal = idMeal;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDayID() {
        return dayID;
    }

    public void setDayID(String dayID) {
        this.dayID = dayID;
    }

    public String getIdMeal() {
        return idMeal;
    }

    public void setIdMeal(String idMeal) {
        this.idMeal = idMeal;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}

