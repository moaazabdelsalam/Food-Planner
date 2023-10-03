package com.project.foodplanner.filter.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.foodplanner.R;
import com.project.foodplanner.model.Country;

import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

    private final Context context;
    private List<Country> countryList;
    private static CountryAdapter instance = null;

    private CountryAdapter(Context context, List<Country> countryList) {
        this.context = context;
        this.countryList = countryList;
    }

    public static CountryAdapter getInstance(Context context, List<Country> countryList) {
        if (instance == null)
            instance = new CountryAdapter(context, countryList);
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
        holder.filterCategoryImg.setVisibility(View.GONE);
        holder.filterCategoryDescription.setVisibility(View.GONE);
        holder.filterCountryImg.setVisibility(View.VISIBLE);
        holder.filterCategoryName.setText(countryList.get(position).getStrArea());
        holder.filterCountryImg.setText(countryList.get(position).getStrArea().substring(0, 1));
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public void updateCategoryList(List<Country> updatedCountryList) {
        countryList = updatedCountryList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView filterCategoryImg;
        TextView filterCategoryName;
        TextView filterCategoryDescription;
        TextView filterCountryImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            filterCategoryImg = itemView.findViewById(R.id.filterImg);
            filterCategoryName = itemView.findViewById(R.id.filterTitleTxt);
            filterCategoryDescription = itemView.findViewById(R.id.filterDescriptionTxt);
            filterCountryImg = itemView.findViewById(R.id.filterCountryImg);
        }
    }
}

