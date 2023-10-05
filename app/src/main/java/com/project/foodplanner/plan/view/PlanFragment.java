package com.project.foodplanner.plan.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.project.foodplanner.R;
import com.project.foodplanner.model.Meal;
import com.project.foodplanner.utils.DummyCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlanFragment extends Fragment {

    private static final String TAG = "TAG plan fragment";
    TabLayout tabLayout;
    ViewPager2 planPager;
    PlanPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<PlanRecyclerViewAdapter> adapterList = new ArrayList<>();
        adapterList.add(new PlanRecyclerViewAdapter(getContext(), new ArrayList<>()));
        adapterList.add(new PlanRecyclerViewAdapter(getContext(), new ArrayList<>()));
        adapterList.add(new PlanRecyclerViewAdapter(getContext(), new ArrayList<>()));
        adapterList.add(new PlanRecyclerViewAdapter(getContext(), new ArrayList<>()));
        adapterList.add(new PlanRecyclerViewAdapter(getContext(), new ArrayList<>()));
        adapterList.add(new PlanRecyclerViewAdapter(getContext(), new ArrayList<>()));
        adapterList.add(new PlanRecyclerViewAdapter(getContext(), new ArrayList<>()));

        ArrayList<String> days = new ArrayList<>();
        days.add("Sat ");
        days.add("Sun ");
        days.add("Mon ");
        days.add("Tue ");
        days.add("Wed ");
        days.add("Thu ");
        days.add("Fri ");

        tabLayout = view.findViewById(R.id.tabLayout);
        planPager = view.findViewById(R.id.viewPager);
        adapter = new PlanPagerAdapter(getContext(), adapterList);

        planPager.setAdapter(adapter);
        planPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        new TabLayoutMediator(tabLayout, planPager, (tab, position) -> {
            if (position > days.size() - 1)
                tab.setText(days.get(position - days.size()) + 1);
            else
                tab.setText(days.get(position) + (position + 1));
        }).attach();

        //adapterList.set(5, );
        //adapterList.get(5).addToList(DummyCache.getInstance().getTodayMealCache());
        /*List<Meal> changedList = adapterList.get(5).getMealList();
        Log.i(TAG, "onViewCreated: changedListSize: " + changedList.size());
        adapterList.set(5, new PlanRecyclerViewAdapter(getContext(), changedList));*/
        //adapter.notifyDataSetChanged();

        TabLayout.Tab tab = tabLayout.getTabAt(5);
        Log.i(TAG, "onViewCreated: tab at 5: " + tab.getText());

        /*planPager.fakeDragBy(5);
        planPager.beginFakeDrag();*/
    }
}