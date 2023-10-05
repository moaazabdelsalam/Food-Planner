package com.project.foodplanner.filterresult.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.foodplanner.R;
import com.project.foodplanner.model.Meal;

import java.util.List;

public class FilterResultAdapter extends RecyclerView.Adapter<FilterResultAdapter.ViewHolder> {

    private final Context context;
    private List<Meal> mealList;
    FilterResultClickListener filterClickListener;

    public FilterResultAdapter(Context context, List<Meal> mealList, FilterResultClickListener filterClickListener) {
        this.context = context;
        this.mealList = mealList;
        this.filterClickListener = filterClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewHolder(inflater.inflate(R.layout.view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        Glide.with(context).load(meal.getStrMealThumb())
                .placeholder(R.drawable.image_placeholder).into(holder.mealImgView);
        holder.mealNameTxt.setText(meal.getStrMeal());
        holder.addFavoriteIcon.setImageResource(meal.isFavorite() ? R.drawable.ic_baseline_favorite_24 : R.drawable.ic_baseline_favorite_border_24);
        holder.addToPlanBtn.setOnClickListener(view -> Log.i("TAG", "onBindViewHolder: showing meal..."));
        holder.mealImgView.setOnClickListener(view -> filterClickListener.showMealDetails(meal));
        holder.addFavoriteIcon.setOnClickListener(view -> {
            if (meal.isFavorite()) {
                filterClickListener.removeFromFavorite(meal);
                meal.setFavorite(false);
                holder.addFavoriteIcon.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            } else {
                filterClickListener.addToFavorite(meal);
                meal.setFavorite(true);
                holder.addFavoriteIcon.setImageResource(R.drawable.ic_baseline_favorite_24);
            }
        });
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
        ImageView mealImgView;
        ImageView addFavoriteIcon;
        TextView mealNameTxt;
        AppCompatButton addToPlanBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImgView = itemView.findViewById(R.id.mealImgView);
            addFavoriteIcon = itemView.findViewById(R.id.addFavoriteIcon);
            mealNameTxt = itemView.findViewById(R.id.mealNameTxt);
            addToPlanBtn = itemView.findViewById(R.id.addToPlanBtn);
        }
    }
}
