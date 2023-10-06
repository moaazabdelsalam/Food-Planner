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

import com.project.foodplanner.R;
import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.SimpleMeal;

import java.util.List;

public class PlanRecyclerViewAdapter extends RecyclerView.Adapter<PlanRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<SimpleMeal> mealList;

    public PlanRecyclerViewAdapter(Context context, List<SimpleMeal> mealList) {
        this.context = context;
        this.mealList = mealList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SimpleMeal meal = mealList.get(position);
        holder.mealNameTxt.setText(meal.getStrMeal());
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

        ImageView mealImgView;
        ImageView addFavoriteIcon;
        TextView mealNameTxt;
        AppCompatButton addToPlanBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mealImgView = itemView.findViewById(R.id.mealImgView);
            addFavoriteIcon = itemView.findViewById(R.id.addFavoriteIcon);
            mealNameTxt = itemView.findViewById(R.id.mealNameTxt);
        }
    }
}
