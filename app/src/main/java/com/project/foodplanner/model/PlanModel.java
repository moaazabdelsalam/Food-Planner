package com.project.foodplanner.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "plan_table", foreignKeys = {@ForeignKey(entity = SimpleMeal.class, parentColumns = "idMeal", childColumns = "idMeal", onDelete = ForeignKey.CASCADE)})
public class PlanModel {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    long id;
    String dayID;
    private String idMeal;

    public PlanModel(long id, String dayID, String idMeal) {
        this.id = id;
        this.dayID = dayID;
        this.idMeal = idMeal;
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
}

