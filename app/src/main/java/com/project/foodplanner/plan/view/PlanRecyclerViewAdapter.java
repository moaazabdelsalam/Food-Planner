package com.project.foodplanner.plan.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.foodplanner.R;
import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.SimpleMeal;

import java.util.List;

public class PlanRecyclerViewAdapter extends RecyclerView.Adapter<PlanRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<SimpleMeal> mealList;
    PlanClickListener planClickListener;

    public PlanRecyclerViewAdapter(Context context, List<SimpleMeal> mealList, PlanClickListener planClickListener) {
        this.context = context;
        this.mealList = mealList;
        this.planClickListener = planClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.plan_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SimpleMeal meal = mealList.get(position);
        Glide.with(context).load(meal.getStrMealThumb()).placeholder(R.drawable.image_placeholder).into(holder.planMealImgView);
        holder.planMealNameTxt.setText(meal.getStrMeal());
        holder.planMealCategory.setText(meal.getStrCategory());
        holder.planMealCountry.setText(meal.getStrArea());
        holder.planMealImgView.setOnClickListener(view -> planClickListener.onMealImgClick(meal.getIdMeal()));
        holder.planMealCountry.setOnClickListener(view -> planClickListener.onCountryTxtClicked(meal.getStrArea()));
        holder.planMealCategory.setOnClickListener(view -> planClickListener.onCategoryTxtClicked(meal.getStrCategory()));
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    public void addToList(SimpleMeal meal) {
        mealList.add(meal);
        notifyDataSetChanged();
    }

    public List<SimpleMeal> getMealList() {
        return mealList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView planMealImgView;
        TextView planMealNameTxt;
        TextView planMealCategory;
        TextView planMealCountry;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            planMealImgView = itemView.findViewById(R.id.planMealImgView);
            planMealNameTxt = itemView.findViewById(R.id.planMealNameTxt);
            planMealCategory = itemView.findViewById(R.id.planMealCategory);
            planMealCountry = itemView.findViewById(R.id.planMealCountry);
        }
    }
}
