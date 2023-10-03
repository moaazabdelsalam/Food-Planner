package com.project.foodplanner.search.view;

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

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private final Context context;
    private List<Ingredient> ingredientList;

    public IngredientsAdapter(Context context, List<Ingredient> ingredientList) {
        this.context = context;
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new IngredientsAdapter.ViewHolder(inflater.inflate(R.layout.category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load("https://www.themealdb.com/images/ingredients/" + ingredientList.get(position).getStrIngredient() + ".png")
                .placeholder(R.drawable.image_placeholder).into(holder.categoryImgView);
        holder.categoryName.setText(ingredientList.get(position).getStrIngredient());
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public void updateIngredientList(List<Ingredient> updatedIngredientList) {
        ingredientList = updatedIngredientList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImgView;
        TextView categoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImgView = itemView.findViewById(R.id.categoryImgView);
            categoryName = itemView.findViewById(R.id.categoryNameTXT);
        }
    }
}
