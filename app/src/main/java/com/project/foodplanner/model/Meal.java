package com.project.foodplanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_meals")
public class Meal implements Parcelable {

    @PrimaryKey
    @NonNull
    private String idMeal;
    private String strMeal;
    private String strCategory;
    private String strArea;
    private String strMealThumb;
    private String strTags;

    @Ignore
    private boolean isFavorite;

    public Meal() {
    }

    public Meal(String idMeal, String strMeal, String strCategory, String strArea, String strMealThumb, String strTags, boolean isFavorite) {
        this.idMeal = idMeal;
        this.strMeal = strMeal;
        this.strCategory = strCategory;
        this.strArea = strArea;
        this.strMealThumb = strMealThumb;
        this.strTags = strTags;
        this.isFavorite = isFavorite;
    }

    protected Meal(Parcel in) {
        idMeal = in.readString();
        strMeal = in.readString();
        strCategory = in.readString();
        strArea = in.readString();
        strMealThumb = in.readString();
        strTags = in.readString();
        isFavorite = in.readByte() != 0;
    }

    public static final Creator<Meal> CREATOR = new Creator<Meal>() {
        @Override
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }

        @Override
        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };

    public String getIdMeal() {
        return idMeal;
    }

    public void setIdMeal(String idMeal) {
        this.idMeal = idMeal;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public void setStrMeal(String strMeal) {
        this.strMeal = strMeal;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }

    public String getStrArea() {
        return strArea;
    }

    public void setStrArea(String strArea) {
        this.strArea = strArea;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }

    public void setStrMealThumb(String strMealThumb) {
        this.strMealThumb = strMealThumb;
    }

    public String getStrTags() {
        return strTags;
    }

    public void setStrTags(String strTags) {
        this.strTags = strTags;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(idMeal);
        parcel.writeString(strMeal);
        parcel.writeString(strCategory);
        parcel.writeString(strArea);
        parcel.writeString(strMealThumb);
        parcel.writeString(strTags);
        parcel.writeByte((byte) (isFavorite ? 1 : 0));
    }
}
