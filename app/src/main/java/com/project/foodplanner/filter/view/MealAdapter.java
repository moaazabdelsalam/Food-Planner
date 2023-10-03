package com.project.foodplanner.filter.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.foodplanner.R;
import com.project.foodplanner.model.Ingredient;
import com.project.foodplanner.model.Meal;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.ViewHolder> {

    private final Context context;
    private List<Meal> mealList;
    FilterClickListener filterClickListener;
    private static MealAdapter instance = null;

    private MealAdapter(Context context, List<Meal> mealList, FilterClickListener filterClickListener) {
        this.context = context;
        this.mealList = mealList;
        this.filterClickListener = filterClickListener;
    }

    public static MealAdapter getInstance(Context context, List<Meal> mealList, FilterClickListener filterClickListener) {
        if (instance == null)
            instance = new MealAdapter(context, mealList, filterClickListener);
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
        Meal meal = mealList.get(position);
        holder.filterCountryImg.setVisibility(View.GONE);
        holder.filterIngredientImg.setVisibility(View.VISIBLE);
        Glide.with(context).load(meal.getStrMealThumb())
                .placeholder(R.drawable.image_placeholder).into(holder.filterIngredientImg);
        holder.filterIngredientName.setText(meal.getStrMeal());
        holder.filterIngredientDescription.setText(meal.getStrArea());
        if (meal.getStrCategory() != null)
            holder.filterIngredientDescription.append(", " + meal.getStrCategory());
        if (meal.getStrTags() != null)
            holder.filterIngredientDescription.append(", " + meal.getStrTags());
        holder.filterCardView.setOnClickListener(view -> filterClickListener.mealClicked(meal));
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
        CardView filterCardView;
        ImageView filterIngredientImg;
        TextView filterIngredientName;
        TextView filterIngredientDescription;
        TextView filterCountryImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            filterCardView = itemView.findViewById(R.id.filterCardView);
            filterIngredientImg = itemView.findViewById(R.id.filterImg);
            filterIngredientName = itemView.findViewById(R.id.filterTitleTxt);
            filterIngredientDescription = itemView.findViewById(R.id.filterDescriptionTxt);
            filterCountryImg = itemView.findViewById(R.id.filterCountryImg);
        }
    }
}