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
import com.project.foodplanner.database.ConcreteLocalSource;
import com.project.foodplanner.model.Meal;
import com.project.foodplanner.model.Repository;
import com.project.foodplanner.model.SimpleMeal;
import com.project.foodplanner.network.MealClient;
import com.project.foodplanner.plan.presenter.PlanPresenter;
import com.project.foodplanner.plan.presenter.PlanPresenterInterface;
import com.project.foodplanner.utils.DummyCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlanFragment extends Fragment implements PlanViewInterface, PlanListener {

    private static final String TAG = "TAG plan fragment";
    ArrayList<PlanRecyclerViewAdapter> adapterList;
    TabLayout tabLayout;
    ViewPager2 planPager;
    PlanPagerAdapter adapter;
    PlanPresenterInterface presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new PlanPresenter(this, Repository.getInstance(MealClient.getInstance(), ConcreteLocalSource.getInstance(getContext())));

        adapterList = new ArrayList<>();
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
        adapter = new PlanPagerAdapter(getContext(), adapterList, this);

        planPager.setAdapter(adapter);
        planPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        new TabLayoutMediator(tabLayout, planPager, (tab, position) -> {
            if (position > days.size() - 1) tab.setText(days.get(position - days.size()) + 1);
            else tab.setText(days.get(position) + (position + 1));
        }).attach();

        //adapterList.set(5, );
        //adapterList.get(5).addToList(DummyCache.getInstance().getTodayMealCache());
        /*List<Meal> changedList = adapterList.get(5).getMealList();
        Log.i(TAG, "onViewCreated: changedListSize: " + changedList.size());
        adapterList.set(5, new PlanRecyclerViewAdapter(getContext(), changedList));*/
        //adapter.notifyDataSetChanged();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String dayID = String.valueOf(tab.getText().charAt(tab.getText().length() - 1));
                Log.i(TAG, "onTabSelected: plans of day: " + dayID);
                getPlanOfDay(dayID);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        TabLayout.Tab tab = tabLayout.getTabAt(5);
        Log.i(TAG, "onViewCreated: tab at 5: " + tab.getText());

        /*planPager.fakeDragBy(5);
        planPager.beginFakeDrag();*/
    }

    @Override
    public void getPlanOfDay(String dayID) {
        presenter.getPlanWithId(dayID);
    }

    @Override
    public void updateAdapterWithMeal(String tabID, SimpleMeal planSimpleMeal) {
        Log.i(TAG, "updateAdapterWithMeal: tab: " + Integer.parseInt(tabID) + " with " + planSimpleMeal);
        adapterList.get(Integer.parseInt(tabID) - 1).addToList(planSimpleMeal);
    }
}