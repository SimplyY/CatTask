package com.example.yuwei.killexam;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
/**
 * Created by yuwei on 15/2/16.
 */

public class CreateTaskFragment extends Fragment
        implements View.OnClickListener ,CalendarDatePickerDialog.OnDateSetListener{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "sectionNumber";
    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";

    private int sectionNumber;
    Button finishDateButton;
    Button spendTimeButton;
    Button remindMethodButton;
    Button taskAttributeButton;
    Button createTaskButton;

    public CreateTaskFragment() {
    }

    public CreateTaskFragment(int sectionNumber) {
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, sectionNumber);
        setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sectionNumber = getArguments().getInt(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_task, container, false);


        finishDateButton = (Button)v.findViewById(R.id.finishDatePicker);
        finishDateButton.setOnClickListener(this);
        spendTimeButton = (Button)v.findViewById(R.id.spendTimePicker);
        spendTimeButton.setOnClickListener(this);
        remindMethodButton = (Button)v.findViewById(R.id.remindMethodPicker);
        remindMethodButton.setOnClickListener(this);
        taskAttributeButton = (Button)v.findViewById(R.id.taskAttributePicker);
        taskAttributeButton.setOnClickListener(this);

        createTaskButton = (Button)v.findViewById(R.id.createTask);
        createTaskButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finishDatePicker:
                finishDataPick();
                break;
        }
    }

    public void finishDataPick(){
        FragmentManager fm = getChildFragmentManager();
        DateTime now = DateTime.now();
        CalendarDatePickerDialog calendarDatePickerDialog = CalendarDatePickerDialog
                .newInstance(this,now.getYear(), now.getMonthOfYear() - 1,
                        now.getDayOfMonth());
        calendarDatePickerDialog.setTargetFragment(this,1);
        calendarDatePickerDialog.show(fm, FRAG_TAG_DATE_PICKER);
//        DatePickerBuilder dpb = new DatePickerBuilder()
//                .setFragmentManager(getChildFragmentManager())
//                .setStyleResId(R.style.MyCustomBetterPickerTheme)
//                .setTargetFragment(this);
//        dpb.show();
    }

    @Override
    public void onDateSet(CalendarDatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        finishDateButton.setText("Year: " + year + "\nMonth: " + monthOfYear + "\nDay: " + dayOfMonth);
    }
    @Override
    public void onResume() {
        // Example of reattaching to the fragment
        super.onResume();
        CalendarDatePickerDialog calendarDatePickerDialog = (CalendarDatePickerDialog) getChildFragmentManager()
                .findFragmentByTag(FRAG_TAG_DATE_PICKER);
        if (calendarDatePickerDialog != null) {
            calendarDatePickerDialog.setOnDateSetListener(this);
        }
    }
}
