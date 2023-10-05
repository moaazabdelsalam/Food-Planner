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
import com.project.foodplanner.model.Category;
import com.project.foodplanner.model.RequestCode;

import java.util.List;
import java.util.ListIterator;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final Context context;
    private List<Category> categoryList;
    FilterClickListener filterClickListener;
    private static CategoryAdapter instance = null;

    private CategoryAdapter(Context context, List<Category> categoryList, FilterClickListener filterClickListener) {
        this.context = context;
        this.categoryList = categoryList;
        this.filterClickListener = filterClickListener;
    }

    public static CategoryAdapter getInstance(Context context, List<Category> categoryList, FilterClickListener filterClickListener) {
        if (instance == null)
            instance = new CategoryAdapter(context, categoryList, filterClickListener);
        return instance;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewHolder(inflater.inflate(R.layout.filter_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.filterCountryImg.setVisibility(View.GONE);
        holder.filterCategoryImg.setVisibility(View.VISIBLE);
        Glide.with(context).load(category.getStrCategoryThumb()).placeholder(R.drawable.image_placeholder).into(holder.filterCategoryImg);
        holder.filterCategoryName.setText(category.getStrCategory());
        holder.filterCategoryDescription.setText(category.getStrCategoryDescription());
        holder.filterCardView.setOnClickListener(view -> filterClickListener.categoryClicked(category.getStrCategory()));
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
        CardView filterCardView;
        ImageView filterCategoryImg;
        TextView filterCategoryName;
        TextView filterCategoryDescription;
        TextView filterCountryImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            filterCardView = itemView.findViewById(R.id.filterCardView);
            filterCategoryImg = itemView.findViewById(R.id.filterImg);
            filterCategoryName = itemView.findViewById(R.id.filterTitleTxt);
            filterCategoryDescription = itemView.findViewById(R.id.filterDescriptionTxt);
            filterCountryImg = itemView.findViewById(R.id.filterCountryImg);
        }
    }
}
