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

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private final Context context;
    private List<Ingredient> ingredientList;
    FilterClickListener filterClickListener;
    private static IngredientAdapter instance = null;

    private IngredientAdapter(Context context, List<Ingredient> ingredientList, FilterClickListener filterClickListener) {
        this.context = context;
        this.ingredientList = ingredientList;
        this.filterClickListener = filterClickListener;
    }

    public static IngredientAdapter getInstance(Context context, List<Ingredient> ingredientList, FilterClickListener filterClickListener) {
        if (instance == null)
            instance = new IngredientAdapter(context, ingredientList, filterClickListener);
        return instance;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new IngredientAdapter.ViewHolder(inflater.inflate(R.layout.filter_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = ingredientList.get(position);
        holder.filterCountryImg.setVisibility(View.GONE);
        holder.filterIngredientImg.setVisibility(View.VISIBLE);
        Glide.with(context).load("https://www.themealdb.com/images/ingredients/" + ingredient.getStrIngredient() + ".png")
                .placeholder(R.drawable.image_placeholder).into(holder.filterIngredientImg);
        holder.filterIngredientName.setText(ingredient.getStrIngredient());
        holder.filterIngredientDescription.setText(ingredient.getStrDescription());
        holder.filterCardView.setOnClickListener(view -> filterClickListener.ingredientClicked(ingredient));
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