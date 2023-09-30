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
import com.project.foodplanner.model.Category;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private final Context context;
    private List<Category> categoryList;

    public CategoriesAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewHolder(inflater.inflate(R.layout.category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(categoryList.get(position).getStrCategoryThumb()).into(holder.categoryImgView);
        holder.categoryName.setText(categoryList.get(position).getStrCategory());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void updateCategoryList(List<Category> updatedCategoryList) {
        categoryList = updatedCategoryList;
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
