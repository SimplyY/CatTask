package com.example.yuwei.killexam.taskFragments;

import android.content.Intent;
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
import com.example.yuwei.killexam.ChooseBelongActivity;
import com.example.yuwei.killexam.MainActivity;
import com.example.yuwei.killexam.database.MyDatabaseHelper;
import com.example.yuwei.killexam.tools.MyDate;
import com.example.yuwei.killexam.R;
import com.example.yuwei.killexam.tools.Task;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yuwei on 15/2/16.
 */

public class CreateTaskFragment extends Fragment
        implements View.OnClickListener , OnValueChangeListener , OnScrollListener,Formatter,CalendarDatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "sectionNumber";
    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";

    private int sectionNumber;

    EditText taskNameText;
    Button finishDateButton;
    NumberPicker spendTimePickerHours;
    NumberPicker spendTimePickerMinutes;
    EditText taskContextText;
    Spinner remindMethodSpinner;
    Spinner taskAttributeSpinner;
    Button createTaskButton;
    private MyDate finishDate;


    private Task newTask = new Task();


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

        newTask = (Task)savedInstanceState.get("task");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInsrtanceState){
        super.onSaveInstanceState(savedInsrtanceState);
        savedInsrtanceState.putSerializable("task", newTask);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_task, container, false);
        initViews(v);
        setTimerForAttributeSpinner();
        return v;
    }

    private void setTimerForAttributeSpinner(){
//        TODO:
    }

    private void initViews(View v){
        initTaskAttributeSpinner(v);

        initTaskNameText(v);

        initTaskContextText(v);

        initfinishDateButton(v);

        initSpendTimePicker(v);

        initRemindMethodSpinner(v);

        initCreateButton(v);
    }


    private void initTaskAttributeSpinner(View v){
        taskAttributeSpinner = (Spinner)v.findViewById(R.id.taskAttributeSpinner);
        taskAttributeSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> taskAttributeAdapter = ArrayAdapter.createFromResource(this.getActivity().getApplicationContext(),
                R.array.task_attribute_array, android.R.layout.simple_spinner_item);

        taskAttributeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskAttributeSpinner.setAdapter(taskAttributeAdapter);

        setAttributeSpinnerPosition();
    }

    private void setAttributeSpinnerPosition(){
        String attribute = newTask.getTaskAttribute();
        if (attribute != null){
            int position = 1;
            if (attribute.equals("一级")){
                position = 1;
            }
            else if (attribute.equals("二级")){
                position = 2;
            }
            else if (attribute.equals("三级") ){
                position = 3;
            }
            else if (attribute.equals("四级")){
                position = 4;
            }

            taskAttributeSpinner.setSelection(position);

        }


    }

    private void initTaskNameText(View v){
        taskNameText = (EditText) v.findViewById(R.id.taskNameText);
        setNameText();
    }

    private void setNameText(){
        String taskName = newTask.getTaskName();
        if (taskName != null){
            taskNameText.setText(taskName);
        }
    }

    private void initfinishDateButton(View v){
        finishDateButton = (Button)v.findViewById(R.id.finishDatePicker);
        finishDateButton.setOnClickListener(this);
    }



    private void initSpendTimePicker(View v){
        spendTimePickerHours = (NumberPicker)v.findViewById(R.id.spendTimePickerHours);
        spendTimePickerHours.setOnScrollListener(this);
        spendTimePickerHours.setFormatter(this);
        spendTimePickerHours.setMaxValue(99);
        spendTimePickerHours.setMinValue(0);

        spendTimePickerMinutes = (NumberPicker)v.findViewById(R.id.spendTimePickerMinutes);
        spendTimePickerMinutes.setOnScrollListener(this);
        spendTimePickerMinutes.setFormatter(this);
        spendTimePickerMinutes.setMaxValue(59);
        spendTimePickerMinutes.setMinValue(0);

        spendTimePickerHours.setValue(newTask.getSpendHours());
        spendTimePickerMinutes.setValue(newTask.getSpendMinutes());

    }

//numberpicker record spendTime
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal){

    }
    @Override
    public void onScrollStateChange(NumberPicker view, int scrollState){

    }
//numberpicker formatter
    @Override
    public String format(int value){
        String template = String.valueOf(value);
        if (value < 10){
            template = "0" + template;
        }
        return template;
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

        setMethodSpinnerPosition();
    }

    private void setMethodSpinnerPosition(){
        String remindMethod = newTask.getRemindMethod();
        int position = 1;
        if (remindMethod.equals("不提醒")){
            position = 1;
        }
        if (remindMethod.equals("每天")){
            position = 2;
        }
        if (remindMethod.equals("每周")){
            position = 3;
        }
        if (remindMethod.equals("每月")){
            position = 4;
        }
        if (remindMethod.equals("每年")){
            position = 5;
        }

        remindMethodSpinner.setSelection(position);
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

    private void initCreateButton(View v){
        createTaskButton = (Button)v.findViewById(R.id.createTask);
        createTaskButton.setOnClickListener(this);
    }


//Buttons onclick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//finishDatePicker on pick
            case R.id.finishDatePicker:
                finishDataPick();
                break;
//createTaskButton onclick
            case R.id.createTask:
                if(check()){
                    getTaskInfo();
                    writeTaskInDataBase();
                    quit();
                }

                break;
        }
    }

    public void finishDataPick(){
        FragmentManager fm = getChildFragmentManager();
        MyDate now = new MyDate();
        CalendarDatePickerDialog calendarDatePickerDialog = CalendarDatePickerDialog
                .newInstance(this,now.getYear(), now.getMonth(),
                        now.getDay());
        calendarDatePickerDialog.setTargetFragment(this, 1);
        calendarDatePickerDialog.show(fm, FRAG_TAG_DATE_PICKER);
    }

//setFinishDate
    @Override
    public void onDateSet(CalendarDatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth){
        finishDate =  new MyDate(year, monthOfYear, dayOfMonth);
        newTask.setFinishedTime(finishDate);
        finishDateButton.setText(finishDate.toString());
    }



    private boolean check(){
        return checkTeskName()&&checkContext()&&checkFinishDate()&&checkTime();
    }

    private boolean checkTeskName(){
        if (checkEditText(taskNameText) == false){
            Toast.makeText(this.getActivity().getApplicationContext(), "任务名不能为空，或者有空格", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(isNameHasExist(taskNameText) == true){
            Toast.makeText(this.getActivity().getApplication(), "任务名已存在", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkEditText(EditText text){
        String template = text.getText().toString();
        if (template.equals("")){
            return false;
        }
        Matcher space = Pattern.compile(" +").matcher(template);
        while (space.find()){
            return false;
        }
        return true;
    }

    private boolean isNameHasExist(EditText text){
        String name = text.getText().toString();
        return MyDatabaseHelper.checkNameHasExist(this.getActivity().getApplicationContext(), name);
    }

    private boolean checkFinishDate(){
        MyDate current = new MyDate();
        boolean checkDate = true;

        if (finishDate == null){
            checkDate = false;
            Toast.makeText(this.getActivity().getApplicationContext(), "任务时间必须选择", Toast.LENGTH_SHORT).show();
            return checkDate;
        }

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
            Toast.makeText(this.getActivity().getApplicationContext(), "任务完成时间不能比当前时间早", Toast.LENGTH_SHORT).show();

        }
        return checkDate;
    }

    private boolean checkContext(){
        if (checkEditText(taskContextText) == false){
            Toast.makeText(this.getActivity().getApplicationContext(), "内容不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkTime(){
        if (spendTimePickerMinutes.getValue() == 0 && spendTimePickerHours.getValue() == 0){
            Toast.makeText(this.getActivity().getApplicationContext(), "时间不能为零", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void getTaskInfo(){

        getTaskName();
        getTaskContext();
        getHours();
        getMinuts();
        getMethod();
        getAttribute();
        getTaskBelong();


    }

    private void getTaskName(){
        String taskName = taskNameText.getText().toString();
        newTask.setTaskName(taskName);
    }

    private void getHours(){
        int spendHours = spendTimePickerHours.getValue();
        newTask.setSpendHours(spendHours);
    }

    private void getMinuts(){
        int spentMinutes = spendTimePickerMinutes.getValue();
        newTask.setSpendMinutes(spentMinutes);
    }

    private void getTaskContext(){
        String taskContext = taskContextText.getText().toString();
        newTask.setTaskContext(taskContext);
    }

    private void getMethod(){
        String remindMethod = remindMethodSpinner.getSelectedItem().toString();
        newTask.setRemindMethod(remindMethod);
    }


    private void getAttribute(){
        String taskAttribute = taskAttributeSpinner.getSelectedItem().toString();
        newTask.setTaskAttribute(taskAttribute);
    }

    private void getTaskBelong(){
        if (newTask.getTaskAttribute().equals("一级") == false){
            newTask.setHasBelong(true);
            setTaskBelong();
        }
        else{
            newTask.setHasBelong(false);
        }
    }



    private void writeTaskInDataBase(){
        MyDatabaseHelper.writeNewTask(this.getActivity().getApplicationContext(), newTask);
    }

    private void quit(){
        if (newTask.isHasBelong() == false){
            quitCreateTaskFragment();
        }
        else{
            setTaskBelong();
        }
    }

    private void quitCreateTaskFragment(){
        MainActivity activity = (MainActivity)this.getActivity();
        android.support.v4.app.FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        MainActivity.PlaceholderFragment placeholderFragment = MainActivity.PlaceholderFragment.newInstance(1);
        fragmentTransaction.replace(R.id.container, placeholderFragment)
                .commit();
    }

    private void setTaskBelong(){
        enterChooseTaskBelongActivity();
    }

    private void enterChooseTaskBelongActivity(){
        MainActivity activity = (MainActivity)this.getActivity();
        Intent intent = new Intent(activity,ChooseBelongActivity.class);
        intent.putExtra("taskName", newTask.getTaskName());
        intent.putExtra("taskAttribute", newTask.getTaskAttribute());
        startActivity(intent);
    }
}
