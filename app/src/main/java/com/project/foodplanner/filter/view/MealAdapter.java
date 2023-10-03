package com.project.foodplanner.filter.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.foodplanner.R;
import com.project.foodplanner.model.Ingredient;
import com.project.foodplanner.model.Meal;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.ViewHolder> {

    private final Context context;
    private List<Meal> mealList;
    private static MealAdapter instance = null;

    private MealAdapter(Context context, List<Meal> mealList) {
        this.context = context;
        this.mealList = mealList;
    }

    public static MealAdapter getInstance(Context context, List<Meal> mealList) {
        if (instance == null)
            instance = new MealAdapter(context, mealList);
        return instance;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new MealAdapter.ViewHolder(inflater.inflate(R.layout.filter_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.filterCountryImg.setVisibility(View.GONE);
        holder.filterIngredientImg.setVisibility(View.VISIBLE);
        Glide.with(context).load(mealList.get(position).getStrMealThumb())
                .placeholder(R.drawable.image_placeholder).into(holder.filterIngredientImg);
        holder.filterIngredientName.setText(mealList.get(position).getStrMeal());
        holder.filterIngredientDescription.setText(mealList.get(position).getStrArea());
        if (mealList.get(position).getStrCategory() != null)
            holder.filterIngredientDescription.append(", " + mealList.get(position).getStrCategory());
        if (mealList.get(position).getStrTags() != null)
            holder.filterIngredientDescription.append(", " + mealList.get(position).getStrTags());
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public void updateMealList(List<Meal> updatedMealList) {
        mealList = updatedMealList;
        notifyDataSetChanged();
    }

    public void clearMealList() {
        mealList.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView filterIngredientImg;
        TextView filterIngredientName;
        TextView filterIngredientDescription;
        TextView filterCountryImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            filterIngredientImg = itemView.findViewById(R.id.filterImg);
            filterIngredientName = itemView.findViewById(R.id.filterTitleTxt);
            filterIngredientDescription = itemView.findViewById(R.id.filterDescriptionTxt);
            filterCountryImg = itemView.findViewById(R.id.filterCountryImg);
        }
    }
}