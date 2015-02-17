package com.example.yuwei.killexam;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker.*;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;

import java.util.ArrayList;

/**
 * Created by yuwei on 15/2/16.
 */

public class CreateTaskFragment extends Fragment
        implements View.OnClickListener , OnValueChangeListener , OnScrollListener,Formatter,CalendarDatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "sectionNumber";
    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";

    private int sectionNumber;
    Button finishDateButton;
    NumberPicker spendTimePicker;
    EditText taskContextText;
    Spinner remindMethodSpinner;
    Spinner taskAttributeSpinner;
    Button createTaskButton;

    private CurrentTime finishDate;
    private int spendTime = 0;
    private String taskContext;
    private String remindMethod;
    private String taskAttribute;



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
        initViews(v);
        return v;
    }

    private void initViews(View v){

        initfinishDateButton(v);

        initSpendTimePicker(v);

        initTaskContextText(v);

        initRemindMethodSpinner(v);

        initTaskAttributeSpinner(v);

        initCreateButton(v);
    }

    private void initfinishDateButton(View v){
        finishDateButton = (Button)v.findViewById(R.id.finishDatePicker);
        finishDateButton.setOnClickListener(this);
    }

    private void initSpendTimePicker(View v){
        spendTimePicker = (NumberPicker)v.findViewById(R.id.spendTimePicker);
        spendTimePicker.setOnScrollListener(this);
        spendTimePicker.setFormatter(this);
        spendTimePicker.setMaxValue(100);
        spendTimePicker.setMinValue(0);

    }

    private void initTaskContextText(View v){
        taskContextText = (EditText)v.findViewById(R.id.taskContentText);

    }

    private void initRemindMethodSpinner(View v){
        remindMethodSpinner = (Spinner)v.findViewById(R.id.remindMethodSpinner);
        remindMethodSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> remindMethodAdapter = ArrayAdapter.createFromResource(this.getActivity().getApplicationContext(),
                R.array.remind_method_array, android.R.layout.simple_spinner_item);

        remindMethodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        remindMethodSpinner.setAdapter(remindMethodAdapter);
    }

    private void initTaskAttributeSpinner(View v){
        taskAttributeSpinner = (Spinner)v.findViewById(R.id.taskAttributeSpinner);
        taskAttributeSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> taskAttributeAdapter = ArrayAdapter.createFromResource(this.getActivity().getApplicationContext(),
                R.array.task_attribute_array, android.R.layout.simple_spinner_item);

        taskAttributeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskAttributeSpinner.setAdapter(taskAttributeAdapter);


    }

    private void initCreateButton(View v){
        createTaskButton = (Button)v.findViewById(R.id.createTask);
        createTaskButton.setOnClickListener(this);
    }

//Buttons onclick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finishDatePicker:
                finishDataPick();
                break;

            case R.id.createTask:
                if(check()){
                    getTaskContext();
                    getAttribute();
                    getMethod();
                    writeTaskInDataBase();
                    quit();
                }

                break;
        }
    }

    public void finishDataPick(){
        FragmentManager fm = getChildFragmentManager();
        CurrentTime now = new CurrentTime();
        CalendarDatePickerDialog calendarDatePickerDialog = CalendarDatePickerDialog
                .newInstance(this,now.getYear(), now.getMonth(),
                        now.getDay());
        calendarDatePickerDialog.setTargetFragment(this, 1);
        calendarDatePickerDialog.show(fm, FRAG_TAG_DATE_PICKER);
    }

    @Override
    public void onDateSet(CalendarDatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        finishDate =  new CurrentTime(year, monthOfYear, dayOfMonth);
        finishDateButton.setText(finishDate.toString());
    }

//  numberpicker record spendTime
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal){
        spendTime = newVal;
    }
    public void onScrollStateChange(NumberPicker view, int scrollState){

    }

//spinner selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
        ((TextView) parent.getChildAt(0)).setTextSize(20);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private boolean check(){
        return checkFinishDate();
    }

    private boolean checkFinishDate(){
        CurrentTime current = new CurrentTime();
        boolean checkDate = true;

        if (finishDate.getYear() < current.getYear()){
            checkDate = false;
        }
        if (finishDate.getYear() == current.getYear()){
            if (finishDate.getMonth() < current.getMonth()){
                checkDate = false;
            }
            if (finishDate.getMonth() == current.getMonth()){
                if (finishDate.getDay() < current.getDay()){
                    checkDate = false;
                }
            }
        }

        if (checkDate == false){
            Toast.makeText(this.getActivity().getApplicationContext(), "任务完成时间不能比当前时间早", Toast.LENGTH_LONG).show();

        }
        return checkDate;
    }

//numberpicker formatter
    //Override
    public String format(int value){
        String template = String.valueOf(value);
        if (value < 10){
            template = "0" + template;
        }
        return template;
    }



    private void getTaskContext(){
        taskContext = taskContextText.getText().toString();
    }

    private void getAttribute(){
        taskAttribute = taskAttributeSpinner.getSelectedItem().toString();
    }
    private void getMethod(){
        remindMethod = remindMethodSpinner.getSelectedItem().toString();
    }

    private void writeTaskInDataBase(){

    }

    private void quit(){

    }
}
