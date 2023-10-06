package com.project.foodplanner.plan.view;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
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
import java.util.Locale;

public class PlanFragment extends Fragment implements PlanViewInterface, PlanListener {

    private static final String TAG = "TAG plan fragment";
    ArrayList<PlanRecyclerViewAdapter> adapterList;
    TabLayout tabLayout;
    ViewPager2 planPager;
    PlanPagerAdapter adapter;
    PlanPresenterInterface presenter;
    Calendar calendar = Calendar.getInstance();

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

        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        adapterList = new ArrayList<>();
        ArrayList<String> tabName = getDates();

        presenter = new PlanPresenter(this, Repository.getInstance(MealClient.getInstance(), ConcreteLocalSource.getInstance(getContext())));
        presenter.getAllPlans();


        /*adapterList.add(new PlanRecyclerViewAdapter(getContext(), new ArrayList<>()));
        adapterList.add(new PlanRecyclerViewAdapter(getContext(), new ArrayList<>()));
        adapterList.add(new PlanRecyclerViewAdapter(getContext(), new ArrayList<>()));
        adapterList.add(new PlanRecyclerViewAdapter(getContext(), new ArrayList<>()));
        adapterList.add(new PlanRecyclerViewAdapter(getContext(), new ArrayList<>()));
        adapterList.add(new PlanRecyclerViewAdapter(getContext(), new ArrayList<>()));
        adapterList.add(new PlanRecyclerViewAdapter(getContext(), new ArrayList<>()));*/


        tabLayout = view.findViewById(R.id.tabLayout);
        planPager = view.findViewById(R.id.viewPager);
        adapter = new PlanPagerAdapter(getContext(), adapterList, this);

        planPager.setAdapter(adapter);
        planPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        Log.i(TAG, "onViewCreated: day of month " + dayOfMonth);
        new TabLayoutMediator(tabLayout, planPager, (tab, position) -> {
            if (position + 1 == dayOfMonth)
                tab.setText("Today");
            else if (position + 2 == dayOfMonth)
                tab.setText("Yesterday");
            else if (position + 1 == dayOfMonth + 1)
                tab.setText("Tomorrow");
            else
                tab.setText(tabName.get(position));
        }).attach();

        planPager.setCurrentItem(dayOfMonth - 1, false);

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
                //getPlanOfDay(dayID);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                String dayID = String.valueOf(tab.getText().charAt(tab.getText().length() - 1));
                Log.i(TAG, "onTabReselected: plans of day: " + dayID);
            }
        });

        /*TabLayout.Tab tab = tabLayout.getTabAt(5);
        Log.i(TAG, "onViewCreated: tab at 5: " + tab.getText());*/

    }

    public ArrayList<String> getDates() {

        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        Log.i(TAG, "getDates: calender, now: " + calendar.getTime());
        //Log.i(TAG, "getDates: current date: " + dayOfMonth + "/" + (month + 1) + "/" + year);
        //Log.i(TAG, "getDates: day of week: " + dayOfWeek);

        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        String[] months = {
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        };
        //Calendar cal = Calendar.getInstance();

        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //System.out.print(df.format(cal.getTime()));
        ArrayList<Integer> days = new ArrayList<>();
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= maxDay; i++) {
            days.add(i);
            adapterList.add(new PlanRecyclerViewAdapter(getContext(), new ArrayList<>()));
            //cal.set(Calendar.DAY_OF_MONTH, i);
            /*String formattedNumber = String.format("%02d", i);
            int parsedNumber = Integer.parseInt(formattedNumber);*/
            //Log.i(TAG, "getDates: day:" + days.get(i - 1));
            //System.out.print(", " + df.format(cal.getTime()));
        }

        ArrayList<String> tabNames = new ArrayList<>();
        days.forEach(day -> {
            int index = (day - 1) % dayNames.length;
            /*Log.i(TAG, "getDates: days of month: " +
                    dayNames[index] +
                    " " + day + "/" +
                    months[month]);*/
            String formattedTabName = String.format(new Locale(Locale.getDefault().getLanguage(), "EG"), "%s %02d/%s", dayNames[index], day, months[month]);
            //Log.i(TAG, "getDates: day: " + formattedTabName);
            tabNames.add(formattedTabName);
        });

        return tabNames;
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