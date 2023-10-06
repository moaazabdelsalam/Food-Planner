package com.project.foodplanner.model;

import android.content.Context;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

public class DayPickerDialog {
    private final FragmentManager fragmentManager;
    MaterialDatePicker.Builder<Long> datePickerBuilder;
    private static MaterialDatePicker<Long> datePicker;
    long today = MaterialDatePicker.todayInUtcMilliseconds();
    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    long month = calendar.getTimeInMillis();

    public static MaterialDatePicker<Long> showDialog(FragmentManager fragmentManager) {
        new DayPickerDialog(fragmentManager);
        return datePicker;
    }

    private DayPickerDialog(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;

        datePickerBuilder = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select day")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds());

        calendar.setTimeInMillis(today);
        calendar.set(Calendar.MONTH, Calendar.OCTOBER);

        // Build constraints.
        CalendarConstraints.Builder constraintsBuilder =
                new CalendarConstraints.Builder()
                        .setStart(month)
                        .setEnd(month);

        datePickerBuilder.setCalendarConstraints(constraintsBuilder.build());
        datePicker = datePickerBuilder.build();
        datePicker.show(fragmentManager, datePicker.toString());
    }

    public Long onPositiveClicked() {
        final Long[] selectedDay = new Long[1];
        datePicker.addOnPositiveButtonClickListener(selection -> {
            selectedDay[0] = selection;
            Log.i("TAG", "onPositiveClicked: " + selection.toString());
        });
        return selectedDay[0];
    }

}
