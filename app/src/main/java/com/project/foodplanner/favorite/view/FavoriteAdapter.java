package com.project.foodplanner.favorite.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.foodplanner.R;
import com.project.foodplanner.model.Meal;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private Context context;
    private List<Meal> favMealList;
    private FavoriteClickListener favoriteClickListener;

    public FavoriteAdapter(Context context, List<Meal> favMealList, FavoriteClickListener favoriteClickListener) {
        this.context = context;
        this.favMealList = favMealList;
        this.favoriteClickListener = favoriteClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.favorite_item_view, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = favMealList.get(position);
        Glide.with(context).load(meal.getStrMealThumb()).placeholder(R.drawable.image_placeholder).into(holder.favMealImgView);
        holder.favMealNameTxt.setText(meal.getStrMeal());
        if (meal.getStrCategory() == null)
            holder.favMealCategory.setVisibility(View.INVISIBLE);
        if (meal.getStrArea() == null)
            holder.favMealCountry.setVisibility(View.INVISIBLE);
        holder.favMealCategory.setText(meal.getStrCategory());
        holder.favMealCountry.setText(meal.getStrArea());
        holder.favIcon.setOnClickListener(view -> favoriteClickListener.onFavIconClicked(meal));
        holder.favMealCategory.setOnClickListener(view -> favoriteClickListener.onCategoryTxtClicked(meal.getStrCategory()));
        holder.favMealCountry.setOnClickListener(view -> favoriteClickListener.onCountryTxtClicked(meal.getStrArea()));
        holder.favMealImgView.setOnClickListener(view -> favoriteClickListener.onImgClicked(meal.getIdMeal()));
    }

    @Override
    public int getItemCount() {
        return favMealList.size();
    }

    public void updateFavMealList(List<Meal> updatedFavMealList) {
        favMealList = updatedFavMealList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView favMealImgView;
        TextView favMealNameTxt;
        TextView favMealCategory;
        TextView favMealCountry;
        Button addToPlanBtn;
        ImageView favIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            favMealImgView = itemView.findViewById(R.id.favMealImgView);
            favMealNameTxt = itemView.findViewById(R.id.favMealNameTxt);
            favMealCategory = itemView.findViewById(R.id.favMealCategory);
            favMealCountry = itemView.findViewById(R.id.favMealCountry);
            addToPlanBtn = itemView.findViewById(R.id.addToPlanBtn);
            favIcon = itemView.findViewById(R.id.removeFavoriteIcon);
        }
    }
}
