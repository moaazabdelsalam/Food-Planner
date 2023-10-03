package com.project.foodplanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Category implements Parcelable {
    String idCategory;
    String strCategory;
    String strCategoryDescription;
    String strCategoryThumb;

    public Category(String idCategory, String strCategory, String strCategoryDescription, String strCategoryThumb) {
        this.idCategory = idCategory;
        this.strCategory = strCategory;
        this.strCategoryDescription = strCategoryDescription;
        this.strCategoryThumb = strCategoryThumb;
    }

    protected Category(Parcel in) {
        idCategory = in.readString();
        strCategory = in.readString();
        strCategoryDescription = in.readString();
        strCategoryThumb = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }

    public String getStrCategoryDescription() {
        return strCategoryDescription;
    }

    public void setStrCategoryDescription(String strCategoryDescription) {
        this.strCategoryDescription = strCategoryDescription;
    }

    public String getStrCategoryThumb() {
        return strCategoryThumb;
    }

    public void setStrCategoryThumb(String strCategoryThumb) {
        this.strCategoryThumb = strCategoryThumb;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(idCategory);
        parcel.writeString(strCategory);
        parcel.writeString(strCategoryDescription);
        parcel.writeString(strCategoryThumb);
    }
}
