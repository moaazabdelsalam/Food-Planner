package com.project.foodplanner.plan.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.foodplanner.R;

import java.util.List;

public class PlanPagerAdapter extends RecyclerView.Adapter<PlanPagerAdapter.ViewHolder> {
    public static final String TAG = "TAG plan adapter";
    private List<PlanRecyclerViewAdapter> adapterList;
    private Context context;

    public PlanPagerAdapter(Context context, List<PlanRecyclerViewAdapter> adapterList) {
        this.context = context;
        this.adapterList = adapterList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_pager_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LinearLayoutManager linearLayout = new LinearLayoutManager(context);
        linearLayout.setOrientation(RecyclerView.VERTICAL);
        holder.planRV.setLayoutManager(linearLayout);
        holder.planRV.setAdapter(adapterList.get(position));
        Log.i(TAG, "onBindViewHolder: adapter list size: " + adapterList.get(position).getItemCount());
    }

    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView planRV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            planRV = itemView.findViewById(R.id.planRV);
        }
    }
}
